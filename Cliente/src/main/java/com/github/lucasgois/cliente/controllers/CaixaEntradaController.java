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
import javafx.stage.Modality;
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
    private TableView<DadoEmail> tb_email;
    @FXML
    private TableColumn<DadoEmail, String> tb_email_assunto;
    @FXML
    private TableColumn<DadoEmail, String> tb_email_remetente;

    private Stage stage;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        btn_atualizar.setVisible(false);

        btn_abrir.setOnAction(event -> handleVizualizaEmail());
        btn_sair.setOnAction(event -> handleSair());
        btn_excluir.setOnAction(event -> handleExcluir());
        btn_atualizar.setOnAction(event -> atualizarTabela());
        btn_escrever.setOnAction(event -> escreverEmail());

        tb_email_assunto.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getAssunto()));
        tb_email_remetente.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getRemetente()));

        ConexaoCliente.SINGLETON.setListaEmails(tb_email.getItems());

        atualizarTabela();
    }

    private void escreverEmail() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tela_envio_email.fxml"));
            final Stage escreverEmailStage = new Stage();
            escreverEmailStage.setScene(new Scene(loader.load()));
            escreverEmailStage.setTitle("Escrever E-mail");
            escreverEmailStage.initModality(Modality.WINDOW_MODAL);
            escreverEmailStage.initOwner(stage);

            final TelaEnvioEmailController telaEnvioEmailController = loader.getController();
            telaEnvioEmailController.showAndWait(escreverEmailStage, null);

        } catch (final IOException ex) {
            erro(ex);
        }
    }

    private void atualizarTabela() {

        //requisição para atualizar a tabela com os email do servidor
        ConexaoCliente.SINGLETON.buscarEmails();
    }

    private void handleExcluir() {
        //aqui fazer uma requisição para excluir o email do servidor e após isso atualizar a tabela

        final DadoEmail emailSelecionado = tb_email.getSelectionModel().getSelectedItem();

        if (emailSelecionado == null) {
            aviso("Nada selecionado.");
            return;
        }

        ConexaoCliente.SINGLETON.excluirEmail(emailSelecionado.getId());
    }

    private void handleVizualizaEmail() {
        //chamar tela de envio de email na versao de vizualizacao

        if (tb_email.getSelectionModel().getSelectedItem() == null) {
            aviso("Nada selecionado.");
            return;
        }

        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tela_envio_email.fxml"));
            final Stage visualizarEmailStage = new Stage();
            visualizarEmailStage.setScene(new Scene(loader.load()));
            visualizarEmailStage.setTitle("Visualização de E-mail");
            visualizarEmailStage.initModality(Modality.WINDOW_MODAL);
            visualizarEmailStage.initOwner(stage);

            final TelaEnvioEmailController telaEnvioEmailController = loader.getController();
            telaEnvioEmailController.showAndWait(visualizarEmailStage, tb_email.getSelectionModel().getSelectedItem());

        } catch (final IOException ex) {
            erro(ex);
        }
    }

    private void handleSair() {
        ConexaoCliente.SINGLETON.desconectar();
        ((Stage) btn_sair.getScene().getWindow()).close();
    }

    void showAndWait() {
        lb_usuario.setText(ConexaoCliente.SINGLETON.getLogin().getNome());

        stage = (Stage) lb_usuario.getScene().getWindow();
        stage.setOnCloseRequest(event -> ConexaoCliente.SINGLETON.desconectar());
        stage.showAndWait();
    }

}
