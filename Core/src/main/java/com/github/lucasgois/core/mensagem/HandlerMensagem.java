package com.github.lucasgois.core.mensagem;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;

public interface HandlerMensagem {

    @SuppressWarnings("ConstantDeclaredInInterface")
    Logger log = LogManager.getLogger(HandlerMensagem.class);

    default void enviaDado(@NotNull final OutputStream outputStream, @NotNull final Dado dado) throws SocketException {
        log.info("para enviar: {}", dado);

        try {
            final byte[] mensagemSerializada = dado.write();

            // Envia o tamanho da mensagem como um inteiro (4 bytes)
            outputStream.write(ByteBuffer.allocate(4).putInt(mensagemSerializada.length).array());

            // Envia a mensagem serializada
            outputStream.write(mensagemSerializada);
            outputStream.flush();

        } catch (final SocketException e) {
            throw e;

        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
    }

    default <T extends Dado> T recebeDado(@NotNull final InputStream inputStream) throws SocketException {
        try {
            final byte[] tamanhoBytes = new byte[4];
            final int result = inputStream.read(tamanhoBytes);
            if (result == -1) throw new ErroRuntimeException("Conexão encerrada");

            final int tamanho = ByteBuffer.wrap(tamanhoBytes).getInt();

            final byte[] recebido = new byte[tamanho];
            int lidos = 0;
            while (lidos < tamanho) {
                final int n = inputStream.read(recebido, lidos, tamanho - lidos);
                if (n == -1) throw new SocketException("Conexão encerrada durante a leitura da mensagem");
                lidos += n;
            }

            final T dado = Dado.read(recebido);
            log.info("recebido: {}", dado);
            return dado;

        } catch (final SocketException ex) {
            throw ex;

        } catch (final IOException e) {
            throw new ErroRuntimeException("Erro ao receber a mensagem", e);
        }
    }

}
