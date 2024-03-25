package com.github.lucasgois.servidor.socket;

import com.github.lucasgois.core.exceptions.AvisoException;
import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.*;
import com.github.lucasgois.core.util.Constantes;
import com.github.lucasgois.servidor.banco.BancoHandler;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@NoArgsConstructor
public class ChatSocketServidor {

    private ServerSocket serverSocket;
    private volatile boolean rodando = false;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private final Collection<SocketCliente> clientes = new ArrayList<>(4);

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

        try {
            DadoLogin login = null;

            clientes.add(cliente);

            // Leitura continua
            while (!socket.isClosed()) {
                final Dado dado = cliente.receber();

                if (dado instanceof final Mensagem mensagem) {
                    enviarTodos(mensagem, cliente.getId());

                } else if (dado instanceof final DadoLogin dadoLogin) {
                    login = dadoLogin;
                    BancoHandler.login(login);
                    cliente.enviar(login);

                } else if (dado instanceof final DadoLogout dadoLogout) {
                    Objects.requireNonNull(login, "Login nulo");
                    log.info("Logout: " + login.getNome());
                    break;

                } else if (dado instanceof final DadoEmail email) {
                    BancoHandler.email(email);

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
            clientes.remove(cliente);

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

    public void enviar(final String texto) throws AvisoException {

        if (clientes.isEmpty()) {
            throw new AvisoException("Não há clientes conectados");
        }

        final Mensagem mensagem = new Mensagem();
        mensagem.setOrigem(Constantes.ID_SERVIDOR);
        mensagem.setTexto(texto);

        enviarTodos(mensagem, Constantes.ID_SERVIDOR);
    }

    private void enviarTodos(final Mensagem mensagem, @Nullable final UUID... naoEnviarPara) {
        for (final SocketCliente cliente : clientes) {

            if (contem(cliente, naoEnviarPara)) {
                continue;
            }

            try {
                cliente.enviar(mensagem);

            } catch (final SocketException ex) {
                clientes.remove(cliente);

            } catch (final Exception ex) {
                throw new ErroRuntimeException(ex);
            }
        }
    }

    private static boolean contem(@NotNull final SocketCliente cliente, @Nullable final UUID[] exceto) {
        for (final UUID uuid : exceto) {
            if (cliente.getId() == uuid) {
                return true;
            }
        }
        return false;
    }

}

