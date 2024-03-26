package com.github.lucasgois.servidor;

import com.github.lucasgois.servidor.banco.HibernateUtil;
import com.github.lucasgois.servidor.socket.ChatSocketServidor;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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

        final ChatSocketServidor chatSocket = new ChatSocketServidor();
        chatSocket.iniciar();

        endereco();
    }

    private void endereco() throws SocketException {
        final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        String ipv4Address = null;
        while (interfaces.hasMoreElements()) {
            final NetworkInterface iface = interfaces.nextElement();
            // Filtra interfaces que estão desligadas ou são loopback
            if (iface.isLoopback() || !iface.isUp()) {
                continue;
            }

            // Itera todos os endereços da interface
            final Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                final InetAddress addr = addresses.nextElement();
                // Verifica se é IPv4
                if (addr.getHostAddress().indexOf(':') < 0) {
                    ipv4Address = addr.getHostAddress();
                    break;
                }
            }

            if (ipv4Address != null) {
                break;
            }
        }
        log.info("Endereco IP: " + ipv4Address);
    }

}