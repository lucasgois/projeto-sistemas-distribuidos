package com.github.lucasgois.cliente;

import com.github.lucasgois.cliente.socket.ConexaoCliente;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.mensagem.DadoUsuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/tela_principal_cliente.fxml"));

        final Parent raiz = loader.load();

        final Scene cena = new Scene(raiz);
        primaryStage.setScene(cena);
        primaryStage.setTitle("Client chat");
        primaryStage.show();


        final ConexaoCliente conexaoCliente = new ConexaoCliente();
        conexaoCliente.conectar();

        final DadoEmail email = new DadoEmail();

        email.setDestinatario(new DadoUsuario("Nome"));
        email.setAssunto("Assunto teste1");

        conexaoCliente.enviarEmail(email);
    }

}
