package com.github.lucasgois.servidor.socket;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.Dado;
import com.github.lucasgois.core.mensagem.HandlerMensagem;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

@Log4j2
class SocketCliente implements HandlerMensagem {

    @Getter
    @Setter
    private UUID id;
    private final Socket socket;

    SocketCliente(final Socket socket) {
        this.socket = socket;
    }

    void enviar(@NotNull final Dado dado) throws SocketException {

        try {
            enviaDado(socket.getOutputStream(), dado);
        } catch (final SocketException e) {
            throw e;

        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
    }

    <T extends Dado> T receber() throws SocketException {
        try {
            return recebeDado(socket.getInputStream());

        } catch (final SocketException ex) {
            throw ex;

        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
    }

}
