package com.github.lucasgois.core.mensagem;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dado implements Serializable {

    private UUID origem;
    private UUID destino;

    public byte[] write() {
        try {
            final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            try (final ObjectOutput objectOutputStream = new ObjectOutputStream(arrayOutputStream)) {
                objectOutputStream.writeObject(this);
                objectOutputStream.flush();
            }
            return arrayOutputStream.toByteArray();
        } catch (final IOException e) {
            throw new ErroRuntimeException(e);
        }
    }

    public static <T extends Dado> T read(final byte[] data) {
        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream(data);
            try (final ObjectInputStream ois = new ObjectInputStream(bais)) {
                @SuppressWarnings("unchecked") final T readObject = (T) ois.readObject();
                return readObject;
            }
        } catch (final IOException | ClassNotFoundException e) {
            throw new ErroRuntimeException(e);
        }
    }

}
