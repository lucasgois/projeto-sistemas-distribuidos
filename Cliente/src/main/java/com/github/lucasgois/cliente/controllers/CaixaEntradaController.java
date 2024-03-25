package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.cliente.ChatSocketCliente;
import com.github.lucasgois.core.exceptions.ErroException;
import com.github.lucasgois.core.exceptions.SemConexaoComServidorException;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.mensagem.Email;
import com.github.lucasgois.core.util.Alerta;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ResourceBundle;

@Log
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

        tb_email_assunto.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getAssunto()));
        tb_email_remetente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getRemetente().getUsuario()));
    }

    private void atualizarTabela(){
        //requisição para atualizar a tabela com os email do servidor
    }

    private void excluiEmail() {
        //aqui fazer uma requisição para excluir o email do servidor e após isso atualizar a tabela
    }

    private void handleVizualizaEmail() {
        //chamar tela de envio de email na versao de vizualizacao
    }

    private void handleSair() {
        Stage stage = (Stage) btn_sair.getScene().getWindow();
        stage.close();
    }
}