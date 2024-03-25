package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.core.util.Alerta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable, Alerta {

    @FXML
    private Button btn_enviar;

    @FXML
    private TextField tf_senha;

    @FXML
    private TextField tf_usuario;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tf_senha.setText("");
        tf_usuario.setText("");
        btn_enviar.setOnAction(event -> conectar());
    }

    private void conectar() {
        //aqui ele fara uma requisição consultando login no servidor, se ja existir valida a senha e entra, se não existir ele cria no banco, [
        // se o servidor tiver fora estora erro

        try {
            //metodo de requisicao conectar
        } catch (Exception ex) {
            erro(ex);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/caixa_entrada.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Caixa de Entrada");

            CaixaEntradaController caixaEntradaController = loader.getController();
            caixaEntradaController.showAndWait(stage, tf_usuario.getText());

        } catch (IOException ex) {
            erro(ex);
        }
    }
}
