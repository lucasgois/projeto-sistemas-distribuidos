package com.github.lucasgois.cliente.socket;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.*;
import com.github.lucasgois.core.util.Constantes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConexaoCliente implements HandlerMensagem {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private Socket socket;

    public void conectar(@NotNull final DadoLogin login) {
        try {
            socket = new Socket("127.0.0.1", Constantes.PORTA_CHAT);
            enviaDado(socket.getOutputStream(), login);
        } catch (final IOException ex) {
            throw new ErroRuntimeException(ex);
        }

        executor.execute(this::aguardarServidor);
    }

    private void aguardarServidor() {
        try {
            final Mensagem mensagem = recebeDado(socket.getInputStream());
            log.info("Recebido: {}", mensagem);

        } catch (final SocketException ex) {
            log.info("finalizado");
            return;

        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }

        try {
            executor.execute(this::aguardarServidor);
        } catch (final Exception ex) {
            log.warn(ex.getMessage());
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

    public void buscarEmails(@NotNull final DadoUsuario remetente) {
        executor.execute(() -> {
            try {
                enviaDado(socket.getOutputStream(), new DadoSolicitarEmail(remetente));
                final Dado dado = recebeDado(socket.getInputStream());
                log.info("BUSCADO: {}", dado);
            } catch (final IOException ex) {
                throw new ErroRuntimeException(ex);
            }
        });
    }

}
