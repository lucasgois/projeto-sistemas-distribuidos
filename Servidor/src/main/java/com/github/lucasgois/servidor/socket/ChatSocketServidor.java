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
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
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
            clientes.add(cliente);

            // Leitura continua
            while (!socket.isClosed()) {
                final Dado dado = cliente.receber();

                if (dado instanceof final Mensagem mensagem) {
                    enviarTodos(mensagem, cliente.getId());

                } else if (dado instanceof final DadoLogin login) {
                    final UUID uuid = BancoHandler.login(login);
                    cliente.enviar(new DadoLogin(uuid));

                } else if (dado instanceof final DadoEmail email) {
                    BancoHandler.email(email);

                } else if (dado instanceof final DadoSolicitarEmail solicitarEmail) {
                    log.info("SOLICITAR EMAIL {}", solicitarEmail);

                    var teste = BancoHandler.buscarEmails(solicitarEmail.getUsuario());
                    log.info("TESTE {}", teste);

//                } else if (dado instanceof final DadoLogout logout) {
//
//                    if (dadoConexao.isStatus()) {
//                        cliente.setId(dadoConexao.getOrigem());
//
//                    } else {
//                        break;
//                    }
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

