package com.github.lucasgois.servidor.socket;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.Dado;
import com.github.lucasgois.core.mensagem.HandlerMensagem;
import com.github.lucasgois.core.mensagem.Mensagem;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

@Log
class ClienteConectado implements HandlerMensagem {

    @Getter
    @Setter
    private UUID id;
    private final Socket socket;

    ClienteConectado(final Socket socket) {
        this.socket = socket;
    }

    void enviar(@NotNull final Mensagem mensagem) throws SocketException {

        try {
            enviaDado(socket.getOutputStream(), mensagem);
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
