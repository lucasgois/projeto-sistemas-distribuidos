package com.github.lucasgois.cliente.controllers;

import com.github.lucasgois.cliente.socket.ConexaoCliente;
import com.github.lucasgois.core.mensagem.DadoAnexo;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.util.Alerta;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class TelaEnvioEmailController implements Initializable, Alerta {

    @FXML
    private Button btn_anexarArquivos;
    @FXML
    private Button btn_vizualizaAnexo;
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
    public void initialize(final URL location, final ResourceBundle resources) {
        btn_enviar.setOnAction(event -> handleEnviar());
        btn_anexarArquivos.setOnAction(event -> handleAnexar());
        btn_vizualizaAnexo.setOnAction(event -> vizualizaAnexo());
        lb_nomeUsuario.setText("");
        tf_para.setText("");
        tf_assunto.setText("");
        tf_texto.setText("");

        tb_anexo_arquivo.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getNomeAnexo()));
    }

    private void vizualizaAnexo() {
        //fazer requisicao para trazer arquivo de anexo e abrilo

        if (tb_anexo.getSelectionModel().getSelectedItem() == null) {
            aviso("Nada selecionado.");
        }

    }

    private void handleAnexar() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo");
        final File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("Arquivo selecionado: " + selectedFile.getAbsolutePath());

//            FileInputStream fis;
//            try {
//                fis = new FileInputStream(selectedFile);
//            } catch (Exception ex) {
//                erro(ex);
//            }
            final DadoAnexo dadoAnexo = new DadoAnexo();
            final byte[] fileBytes = new byte[(int) selectedFile.length()];
            dadoAnexo.setAnexo(fileBytes);
            dadoAnexo.setNomeAnexo(selectedFile.getName());
            tb_anexo.getItems().add(dadoAnexo);
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
    }

    private void handleEnviar() {
        //quando a tela estiver no modo de escrever email, aqui colocar a requisicao de envio para o servidor
        try {
            final DadoEmail email = new DadoEmail();

            email.setAssunto(tf_assunto.getText());
            email.setTexto(tf_texto.getText());
            email.setDestinatario(tf_para.getText());
            email.setRemetente(ConexaoCliente.SINGLETON.getLogin().getNome());
//            email.setAnexos(tb_anexo.getItems());

            ConexaoCliente.SINGLETON.enviarEmail(email);
            aviso("E-mail enviado.");

            stage.close();

        } catch (final Exception ex) {
            aviso("Usuario " + tf_para.getText() + "não existe.");
        }
    }

    public void showAndWait(final Stage stage, final int id) {

        this.stage = stage;

        if (id != 0) {
            infoEmail(id);
            tf_para.setEditable(false);
            tf_assunto.setEditable(false);
            tf_texto.setEditable(false);
            btn_enviar.setVisible(false);
            btn_anexarArquivos.setVisible(false);
            btn_vizualizaAnexo.setVisible(true);
        } else {
            tf_para.setEditable(true);
            tf_assunto.setEditable(true);
            tf_texto.setEditable(true);
            btn_enviar.setVisible(true);
            btn_anexarArquivos.setVisible(true);
            btn_vizualizaAnexo.setVisible(false);
        }
        stage.showAndWait();
    }

    private void infoEmail(final int id) {
        //dados do email via requisição onde trara tudo menos o anexo, só o nome dele pelo ID


        lb_nomeUsuario.setText("Usuário: " + "remetente");
        tf_para.setText("Usuario destinatario");
        tf_assunto.setText("Esse é um dado mocado para teste");
        tf_texto.setText("Esse é um dado mocado para teste texto");
    }
}
