package com.github.lucasgois.cliente;

import com.github.lucasgois.core.exceptions.ErroException;
import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.exceptions.SemConexaoComServidorException;
import com.github.lucasgois.core.mensagem.DadoConexao;
import com.github.lucasgois.core.mensagem.HandlerMensagem;
import com.github.lucasgois.core.mensagem.Mensagem;
import com.github.lucasgois.core.util.Constantes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;

@NoArgsConstructor
public class ChatSocketCliente implements HandlerMensagem {

    @Getter
    private final UUID idChat = UUID.randomUUID();

    private Socket socket;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Setter
    private Consumer<String> consumer;

    public void conectar() throws ErroException {
        final String serverAddress = "127.0.0.1";

        try {
            socket = new Socket(serverAddress, Constantes.PORTA_CHAT);

            final DadoConexao dadoConexao = new DadoConexao(true);
            dadoConexao.setOrigem(idChat);
            dadoConexao.setDestino(Constantes.ID_SERVIDOR);
            enviaDado(socket.getOutputStream(), dadoConexao);

        } catch (final ConnectException ex) {
            throw new ErroException("Conexão recusada", ex);

        } catch (final IOException ex) {
            throw new ErroException("Problema", ex);
        }

        executor.execute(this::aguardarServidor);
    }

    private void aguardarServidor() {
        try {
            final Mensagem mensagem = recebeDado(socket.getInputStream());
            consumer.accept(mensagem.formatar());

        } catch (final SocketException ex) {
            log.log(Level.INFO, "finalizado");
            return;

        } catch (final IOException e) {
            fecharConexao();
            throw new ErroRuntimeException(e);
        }

        try {
            executor.execute(this::aguardarServidor);
        } catch (final Exception ex) {
            log.log(Level.WARNING, ex.getMessage());
        }
    }

    public void enviar(final String texto) throws SemConexaoComServidorException {

        if (socket == null) {
            throw new SemConexaoComServidorException();
        }

        executor.execute(() -> {
            try {
                final Mensagem mensagem = new Mensagem();
                mensagem.setDestino(Constantes.ID_SERVIDOR);
                mensagem.setOrigem(idChat);
                mensagem.setTexto(texto);

                enviaDado(socket.getOutputStream(), mensagem);

                consumer.accept(mensagem.formatar());

            } catch (final IOException e) {
                throw new ErroRuntimeException(e);
            }
        });
    }

    public void fecharConexao() {
        try {
            final DadoConexao dadoConexao = new DadoConexao(false);
            dadoConexao.setOrigem(idChat);
            enviaDado(socket.getOutputStream(), dadoConexao);

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
        executor.shutdownNow();
    }

}
