package com.github.lucasgois.servidor;

import com.github.lucasgois.servidor.banco.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class MainServidor extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull final Stage primaryStage) throws Exception {

        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.getTransaction().commit();
        }

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/tela_principal_servidor.fxml"));

        final Parent raiz = loader.load();

        final Scene cena = new Scene(raiz);
        primaryStage.setScene(cena);
        primaryStage.setTitle("Servidor chat");
        primaryStage.show();
    }

}