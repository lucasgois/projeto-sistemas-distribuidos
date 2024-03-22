package com.github.lucasgois.servidor;

import com.github.lucasgois.servidor.banco.HibernateUtil;
import com.github.lucasgois.servidor.banco.entidades.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class Main extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull final Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/tela_principal_servidor.fxml"));

        final Parent raiz = loader.load();

        final Scene cena = new Scene(raiz);
        primaryStage.setScene(cena);
        primaryStage.setTitle("Titulo da Minha Aplicação");
        primaryStage.show();


    }

    private void teste() {
        //        Conexao.init();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Usuario entidade = new Usuario();
        entidade.setNome("Teste");
        entidade.setSenha("123".getBytes(StandardCharsets.UTF_8));
        session.save(entidade);

        session.getTransaction().commit();
        session.close();

        HibernateUtil.shutdown();

    }

}