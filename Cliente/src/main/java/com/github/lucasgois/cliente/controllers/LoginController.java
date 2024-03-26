package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.cliente.socket.ConexaoCliente;
import com.github.lucasgois.core.mensagem.DadoLogin;
import com.github.lucasgois.core.util.Alerta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class LoginController implements Initializable, Alerta {

    @FXML
    private TextField tf_endereco;
    @FXML
    private TextField tf_usuario;
    @FXML
    private PasswordField tf_senha;
    @FXML
    private Button btn_enviar;


    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        tf_endereco.setText(ConexaoCliente.SINGLETON.getEndereco());
        tf_usuario.setText("");
        tf_senha.setText("");
        btn_enviar.setOnAction(event -> conectar());
    }

    private void conectar() {
        //aqui ele fara uma requisição consultando login no servidor, se ja existir valida a senha e entra, se não existir ele cria no banco, [
        // se o servidor tiver fora estora erro

        try {
            ConexaoCliente.SINGLETON.setEndereco(tf_endereco.getText());

            ConexaoCliente.SINGLETON.conectar(new DadoLogin(tf_usuario.getText(), tf_senha.getText()));

            while (ConexaoCliente.SINGLETON.getErro().get() == null) {
            }

            if (ConexaoCliente.SINGLETON.getErro().get().isBlank()) {
                ((Stage) tf_usuario.getScene().getWindow()).close();

                final FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/caixa_entrada.fxml"));
                final Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Caixa de Entrada");

                ((CaixaEntradaController) loader.getController()).showAndWait();

            } else {
                aviso(ConexaoCliente.SINGLETON.getErro().get());
            }


        } catch (final Exception ex) {
            erro(ex);
        }
    }
}
