package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.core.mensagem.DadoAnexo;
import com.github.lucasgois.core.util.Alerta;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaEnvioEmailController implements Initializable, Alerta {

    @FXML
    private Button btn_anexarArquivos;

    @FXML
    private TableView<DadoAnexo> tb_anexo;

    @FXML
    private TableColumn<DadoAnexo, String> tb_anexo_arquivo;

    @FXML
    private Button btn_enviar;

    @FXML
    private Label lb_nomeUsuario;

    @FXML
    private TextField tf_assunto;

    @FXML
    private TextField tf_para;

    @FXML
    private TextArea tf_texto;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_enviar.setOnAction(event -> handleEnviar());
        btn_anexarArquivos.setOnAction(event -> handleAnexar());
        lb_nomeUsuario.setText("");
        tf_para.setText("");
        tf_assunto.setText("");
        tf_texto.setText("");

        tb_anexo_arquivo.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getNomeAnexo()));
    }

    private void handleAnexar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo");
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("Arquivo selecionado: " + selectedFile.getAbsolutePath());


//            FileInputStream fis;
//            try {
//                fis = new FileInputStream(selectedFile);
//            } catch (Exception ex) {
//                erro(ex);
//            }
            DadoAnexo dadoAnexo = new DadoAnexo();
            byte[] fileBytes = new byte[(int) selectedFile.length()];
            dadoAnexo.setAnexo(fileBytes);
            dadoAnexo.setNomeAnexo(selectedFile.getName());
            tb_anexo.getItems().add(dadoAnexo);
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
    }

    private void handleEnviar() {
        //quando a tela estiver no modo de escrever email, aqui colocar a requisicao de envio para o servidor
    }

    public void showAndWait(Stage stage, int id) {

        this.stage = stage;

        if (id != 0) {
            infoEmail();
            tf_para.setDisable(true);
            tf_assunto.setDisable(true);
            tf_texto.setDisable(true);
            btn_enviar.setVisible(false);
            btn_anexarArquivos.setDisable(true);
        } else {
            tf_para.setDisable(false);
            tf_assunto.setDisable(false);
            tf_texto.setDisable(false);
            btn_enviar.setVisible(true);
            btn_anexarArquivos.setDisable(false);
        }
        stage.showAndWait();
    }

    private void infoEmail() {
        //dados do email via requisição onde trara tudo menos o anexo, só o nome dele
        lb_nomeUsuario.setText("Usuario Remetente");
        tf_para.setText("Usuario destinatario");
        tf_assunto.setText("Esse é um dado mocado para teste");
        tf_texto.setText("Esse é um dado mocado para teste texto");
    }
}
