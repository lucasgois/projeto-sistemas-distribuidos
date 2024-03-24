package com.github.lucasgois.servidor;

import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.servidor.banco.HibernateUtil;
import com.github.lucasgois.servidor.banco.entidades.Email;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

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
    static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public static void email(DadoEmail email) {
        //        Conexao.init();


        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Email entidade = new Email();

        entidade.setAssunto(email.getAssunto());

        session.save(entidade);

        session.getTransaction().commit();
        session.close();

//        HibernateUtil.shutdown();

    }

}