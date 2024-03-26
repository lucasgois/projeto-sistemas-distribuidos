package com.github.lucasgois.cliente.socket;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.*;
import com.github.lucasgois.core.util.Alerta;
import com.github.lucasgois.core.util.Constantes;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("java:S654")
public final class ConexaoCliente implements HandlerMensagem, Alerta {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private Socket socket;

    public static final ConexaoCliente SINGLETON = new ConexaoCliente();

    @Getter
    @Setter
    private DadoLogin login = null;

    @Setter
    private ObservableList<DadoEmail> listaEmails;
    @Getter
    private final AtomicReference<String> erro = new AtomicReference<>(null);
    @Getter
    @Setter
    private String endereco = "127.0.0.1";

    private ConexaoCliente() {
    }

    public void conectar(@NotNull final DadoLogin login) {
        try {
            erro.set(null);
            socket = new Socket(endereco, Constantes.PORTA_CHAT);
            enviaDado(socket.getOutputStream(), login);
            this.login = login;

        } catch (final ConnectException ex) {
            throw new ErroRuntimeException("Sem conexão com o servidor", ex);

        } catch (final IOException ex) {
            throw new ErroRuntimeException(ex);
        }

        executor.execute(this::aguardarServidor);
    }

    private void aguardarServidor() {
        try {
            final Dado mensagem = recebeDado(socket.getInputStream());

            if (mensagem instanceof DadoLogin) {
                erro.set("");

            } else if (mensagem instanceof final DadoListaEmail listaEmail) {
                listaEmails.clear();
                listaEmails.addAll(listaEmail.getLista());

            } else if (mensagem instanceof final DadoErro dadoErro) {
                erro.set(dadoErro.getMensagem());
                return;

            } else {
                throw new ErroRuntimeException("Tratar: " + mensagem);
            }

        } catch (final SocketException ex) {
            log.info("Cliente encerrado");
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

    public void buscarEmails() {
        executor.execute(() -> {
            try {
                enviaDado(socket.getOutputStream(), new DadoSolicitarEmail());
            } catch (final IOException ex) {
                throw new ErroRuntimeException(ex);
            }
        });
    }

    private void logout() {
        try {
            enviaDado(socket.getOutputStream(), new DadoLogout());
        } catch (final IOException ex) {
            throw new ErroRuntimeException(ex);
        }
    }

    public void desconectar() {
        logout();

        try {
            socket.close();
        } catch (final IOException ex) {
            throw new ErroRuntimeException(ex);
        }

        executor.shutdown();
    }

    public void excluirEmail(final int id) {
        executor.execute(() -> {
            try {
                enviaDado(socket.getOutputStream(), new DadoExcluirEmail(id));
            } catch (final IOException ex) {
                throw new ErroRuntimeException(ex);
            }
        });
    }
}
