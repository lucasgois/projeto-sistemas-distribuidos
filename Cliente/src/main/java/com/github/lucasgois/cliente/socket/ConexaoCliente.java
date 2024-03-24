package com.github.lucasgois.cliente.socket;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.*;
import com.github.lucasgois.core.util.Constantes;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class ConexaoCliente implements HandlerMensagem {

    private Socket socket;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void conectar() {
        try {
            socket = new Socket("127.0.0.1", Constantes.PORTA_CHAT);
            enviaDado(socket.getOutputStream(), DadoConexao.ONLINE);
            enviaDado(socket.getOutputStream(), new DadoUsuario("Lucas", "123".getBytes(StandardCharsets.UTF_8)));
        } catch (final IOException ex) {
            throw new ErroRuntimeException(ex);
        }

        executor.execute(this::aguardarServidor);
    }

    private void aguardarServidor() {
        try {
            final Mensagem mensagem = recebeDado(socket.getInputStream());
            log.log(Level.INFO, "Recebido: {0}", mensagem);

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

    public void enviarEmail(final DadoEmail email) {
        executor.execute(() -> {
            try {
                enviaDado(socket.getOutputStream(), email);
            } catch (final IOException ex) {
                throw new ErroRuntimeException(ex);
            }
        });
    }

    public void fecharConexao() {
        try {
            enviaDado(socket.getOutputStream(), DadoConexao.OFFLINE);

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
        executor.shutdownNow();
    }

}
