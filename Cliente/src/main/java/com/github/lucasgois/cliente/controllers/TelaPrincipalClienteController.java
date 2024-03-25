package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.cliente.socket.ConexaoCliente;
import com.github.lucasgois.core.util.Alerta;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@SuppressWarnings("java:S116")
public class TelaPrincipalClienteController implements Initializable, Alerta {

    @FXML
    private TextField txf_id;
    @FXML
    private TextField txf_nome;
    @FXML
    private TextArea txa_chat;
    @FXML
    private TextField txf_mensagem;
    @FXML
    private Button btn_enviar;
    @FXML
    private Button btn_conectar;

    private final ConexaoCliente socketCliente = new ConexaoCliente();

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

//        txf_id.setText(socketCliente.getIdChat().toString());
//        socketCliente.setConsumer(texto -> txa_chat.appendText(texto + "\n"));

        txf_mensagem.setOnAction(this::onAction);
        btn_enviar.setOnAction(this::onAction);
        btn_conectar.setOnAction(this::onConectar);

        Platform.runLater(() -> {
            final Stage stage = (Stage) btn_enviar.getScene().getWindow();
            txf_mensagem.requestFocus();
        });
    }

    private void onConectar(final ActionEvent event) {
//        try {
//            socketCliente.conectar();
//        } catch (final Exception ex) {
//            erro(ex);
//        }
    }

    private void onAction(final ActionEvent event) {
        try {
            socketCliente.enviarEmail(null);
            // txf_mensagem.clear();

        } catch (final Exception ex) {
            erro(ex);
        }
    }

}
