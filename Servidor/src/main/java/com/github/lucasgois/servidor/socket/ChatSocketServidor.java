package com.github.lucasgois.servidor.socket;

import com.github.lucasgois.core.exceptions.AvisoException;
import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.Dado;
import com.github.lucasgois.core.mensagem.DadoConexao;
import com.github.lucasgois.core.mensagem.Mensagem;
import com.github.lucasgois.core.util.Constantes;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@NoArgsConstructor
public class ChatSocketServidor {

    private ServerSocket serverSocket;
    private volatile boolean rodando = false;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    @Setter
    private Consumer<String> consumer;

    private final Collection<ClienteConectado> clientes = new ArrayList<>(4);

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
        final ClienteConectado cliente = new ClienteConectado(socket);

        try {
            clientes.add(cliente);

            // Leitura continua
            while (!socket.isClosed()) {
                final Dado dado = cliente.receber();

                if (dado instanceof final Mensagem mensagem) {
                    consumer.accept(dado.getOrigem() + ": " + mensagem.getTexto());

                } else if (dado instanceof final DadoConexao dadoConexao) {

                    if (dadoConexao.isStatus()) {
                        cliente.setId(dadoConexao.getOrigem());
                        consumer.accept(dado.getOrigem() + ": conectou");

                    } else {
                        consumer.accept(cliente.getId() + ": desconectou");
                        break;
                    }
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

        for (final ClienteConectado cliente : clientes) {
            try {
                final Mensagem mensagem = new Mensagem();
                mensagem.setOrigem(Constantes.ID_SERVIDOR);
                mensagem.setTexto(texto);

                cliente.enviar(mensagem);

                consumer.accept(mensagem.formatar());

            } catch (final SocketException ex) {
                clientes.remove(cliente);

            } catch (final Exception ex) {
                throw new ErroRuntimeException(ex);
            }
        }
    }
}

