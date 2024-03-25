package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.cliente.socket.ConexaoCliente;
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
    @FXML
    private Button btn_atualizar;
    @FXML
    private Button btn_escrever;
    @FXML
    private Label lb_usuario;
    @FXML
    private TableView<DadoEmail> tb_produto;
    @FXML
    private TableColumn<DadoEmail, String> tb_email_assunto;
    @FXML
    private TableColumn<DadoEmail, String> tb_email_remetente;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        btn_abrir.setOnAction(event -> handleVizualizaEmail());
        btn_sair.setOnAction(event -> handleSair());
        btn_excluir.setOnAction(event -> excluiEmail());
        btn_atualizar.setOnAction(event -> atualizarTabela());
        btn_escrever.setOnAction(event -> escreverEmail());

        tb_email_assunto.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getAssunto()));
        tb_email_remetente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getRemetente()));

        DadoEmail dadoEmail = new DadoEmail();
        dadoEmail.setId(2);
        dadoEmail.setRemetente("Remetente");
        dadoEmail.setAssunto("Assunto teste");

        tb_produto.getItems().addAll(dadoEmail);
    }

    private void escreverEmail() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tela_envio_email.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Escrever E-mail");

            TelaEnvioEmailController telaEnvioEmailController = loader.getController();
            telaEnvioEmailController.showAndWait(stage, 0);

        } catch (IOException ex) {
            erro(ex);
        }
    }

    private void atualizarTabela() {
        //requisição para atualizar a tabela com os email do servidor
    }

    private void excluiEmail() {
        //aqui fazer uma requisição para excluir o email do servidor e após isso atualizar a tabela
    }

    private void handleVizualizaEmail() {
        //chamar tela de envio de email na versao de vizualizacao

        if (tb_produto.getSelectionModel().getSelectedItem() == null) {
            aviso("Nada selecionado.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tela_envio_email.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Vizualiza Email");

            TelaEnvioEmailController telaEnvioEmailController = loader.getController();
            telaEnvioEmailController.showAndWait(stage, tb_produto.getSelectionModel().getSelectedItem().getId());

        } catch (IOException ex) {
            erro(ex);
        }
    }

    private void handleSair() {
        ConexaoCliente.SINGLETON.desconectar();
        ((Stage) btn_sair.getScene().getWindow()).close();
    }

    public void showAndWait() {
        lb_usuario.setText(ConexaoCliente.SINGLETON.getLogin().getNome());

        final Stage stage = (Stage) lb_usuario.getScene().getWindow();
        stage.setOnCloseRequest(event -> ConexaoCliente.SINGLETON.desconectar());
        stage.showAndWait();
    }

}
