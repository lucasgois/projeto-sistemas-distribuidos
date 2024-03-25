package com.github.lucasgois.cliente;

import com.github.lucasgois.cliente.exemplo.ExemploDeUso;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainCliente extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/login.fxml"));

        final Parent raiz = loader.load();

        final Scene cena = new Scene(raiz);
        primaryStage.setScene(cena);
        primaryStage.setTitle("Client chat");
        primaryStage.show();
    }

    private static void exemploDeUso() {

        final ExemploDeUso exemplo = new ExemploDeUso();
        exemplo.login();
        exemplo.enviarEmail();
        exemplo.buscarEmail();
    }

}
