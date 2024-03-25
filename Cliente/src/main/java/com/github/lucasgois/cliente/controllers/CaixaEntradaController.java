package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.util.Alerta;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@SuppressWarnings("java:S116")
public class CaixaEntradaController implements Initializable, Alerta {

    @FXML
    private Button btn_excluir;
    @FXML
    private Button btn_sair;
    @FXML
    private Button btn_abrir;
//    @FXML
//    private Button btn_atualizar;
    @FXML
    private Label lb_usuario;
    @FXML
    private TableView<DadoEmail> tb_produto;
    @FXML
    private TableColumn<DadoEmail, String> tb_email_assunto;
    @FXML
    private TableColumn<DadoEmail, String> tb_email_remetente;

    private DadoEmail objetoEmail;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        btn_abrir.setOnAction(event -> handleVizualizaEmail());
        btn_sair.setOnAction(event -> handleSair());
        btn_excluir.setOnAction(event -> excluiEmail());
//        btn_atualizar.setOnAction(event -> atualizarTabela());

        tb_email_assunto.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getAssunto()));
        tb_email_remetente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getRemetente()));
    }

    private void atualizarTabela(){
        //requisição para atualizar a tabela com os email do servidor
    }

    private void excluiEmail() {
        //aqui fazer uma requisição para excluir o email do servidor e após isso atualizar a tabela
    }

    private void handleVizualizaEmail() {
        //chamar tela de envio de email na versao de vizualizacao
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tela_envio_email.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Vizualiza Email");

            TelaEnvioEmailController telaEnvioEmailController = loader.getController();
            telaEnvioEmailController.showAndWait(stage, objetoEmail.getId());

        } catch (IOException ex) {
            erro(ex);
        }
    }

    private void handleSair() {
        Stage stage = (Stage) btn_sair.getScene().getWindow();
        stage.close();
    }

    public void showAndWait(Stage stage, String usuario) {
        lb_usuario.setText(usuario);
        stage.showAndWait();
    }
}
