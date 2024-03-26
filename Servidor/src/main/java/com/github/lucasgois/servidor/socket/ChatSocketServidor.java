package com.github.lucasgois.servidor.socket;

import com.github.lucasgois.core.exceptions.DestinatarioNaoExisteRuntimeException;
import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.*;
import com.github.lucasgois.core.util.Constantes;
import com.github.lucasgois.servidor.banco.BancoHandler;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@NoArgsConstructor
public class ChatSocketServidor {

    private ServerSocket serverSocket;
    private volatile boolean rodando = false;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private final Map<String, SocketCliente> clientes = new HashMap<>(4);

    public void iniciar() {
        pool.execute(() -> {
            try {
                iniciarImpl();

            } catch (final Exception e) {
                throw new ErroRuntimeException(e);

            } finally {
                parar();
            }
        });
    }

    private void iniciarImpl() throws IOException {
        serverSocket = new ServerSocket(Constantes.PORTA_CHAT);
        rodando = true;

        while (rodando) {
            @SuppressWarnings("SocketOpenedButNotSafelyClosed") final Socket clientSocket = serverSocket.accept();
            pool.execute(() -> handleClient(clientSocket));
        }
    }

    @SuppressWarnings({"java:S1163", "ChainOfInstanceofChecks"})
    private void handleClient(final Socket socket) {
        final SocketCliente cliente = new SocketCliente(socket);
        DadoLogin login = null;

        try {

            // Leitura continua
            while (!socket.isClosed()) {
                final Dado dado = cliente.receber();

                if (dado instanceof final DadoLogin dadoLogin) {
                    login = dadoLogin;

                    if (BancoHandler.login(login)) {
                        cliente.enviar(login);
                        clientes.put(login.getNome(), cliente);
                    } else {
                        cliente.enviar(new DadoErro("Usuário ou senha inválidos"));
                        break;
                    }

                } else if (dado instanceof final DadoLogout dadoLogout) {
                    Objects.requireNonNull(login, "Login nulo");
                    log.info("Logout: " + login.getNome());
                    break;

                } else if (dado instanceof final DadoEmail email) {

                    try {
                        BancoHandler.email(email);
                    } catch (final DestinatarioNaoExisteRuntimeException ex) {
                        email.setAssunto("Não enviado: " + email.getAssunto());
                        email.setTexto("%s%n%nO email não foi enviado porque o destinatário '%s' não foi encontrado".formatted(email.getTexto(), email.getDestinatario()));
                        email.setDestinatario(email.getRemetente());
                        BancoHandler.email(email);
                    }

                    final SocketCliente clienteDestinatario = clientes.get(email.getDestinatario());

                    if (clienteDestinatario == null) {
                        log.warn("Email para o destinatário '" + email.getDestinatario() + "' não offline");

                    } else {
                        final List<DadoEmail> dadoListaEmail = BancoHandler.buscarEmails(email.getDestinatario());
                        clienteDestinatario.enviar(new DadoListaEmail(dadoListaEmail));
                    }

                } else if (dado instanceof DadoSolicitarEmail) {
                    Objects.requireNonNull(login, "Login nulo");

                    final List<DadoEmail> dadoListaEmail = BancoHandler.buscarEmails(login.getNome());
                    cliente.enviar(new DadoListaEmail(dadoListaEmail));

                } else if (dado instanceof final DadoExcluirEmail excluirEmail) {
                    Objects.requireNonNull(login, "Login nulo");

                    BancoHandler.excluirEmail(excluirEmail.getId());

                    final List<DadoEmail> dadoListaEmail = BancoHandler.buscarEmails(login.getNome());
                    cliente.enviar(new DadoListaEmail(dadoListaEmail));

                } else {
                    throw new ErroRuntimeException("Tratar: " + dado);
                }
            }

            socket.close();

        } catch (final SocketException e) {
            Objects.requireNonNull(login, "Login nulo");
            clientes.remove(login.getNome());

        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
    }

    private void parar() {
        try {
            rodando = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            pool.shutdown();
        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
    }

}
