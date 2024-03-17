package com.github.lucasgois.servidor.controllers;

import com.github.lucasgois.core.exceptions.AvisoException;
import com.github.lucasgois.core.util.Alerta;
import com.github.lucasgois.core.util.Constantes;
import com.github.lucasgois.servidor.socket.ChatSocketServidor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@SuppressWarnings("java:S116")
public class TelaPrincipalServidorController implements Initializable, Alerta {

    @FXML
    private TextArea txa_chat;
    @FXML
    private TextField txf_mensagem;
    @FXML
    private TextField txf_id_chat;
    @FXML
    private Button btn_enviar;

    private final ChatSocketServidor chatSocket = new ChatSocketServidor();
    private final Consumer<String> consumer = texto -> txa_chat.appendText(texto + "\n");

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        txf_id_chat.setText(Constantes.ID_SERVIDOR.toString());

        chatSocket.setConsumer(consumer);

        txf_mensagem.setOnAction(this::onAction);
        btn_enviar.setOnAction(this::onAction);

        chatSocket.iniciar();

        Platform.runLater(() -> txf_mensagem.requestFocus());
    }

    private void onAction(final ActionEvent event) {
        final String texto = txf_mensagem.getText();

        if (texto.isBlank()) {
            aviso("Escreva uma mensagem");
            return;
        }

        try {
            chatSocket.enviar(texto);
            txf_mensagem.clear();

        } catch (final AvisoException ex) {
            aviso(ex);

        } catch (final Exception ex) {
            erro(ex);
        }
    }
}
