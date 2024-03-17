package com.github.lucasgois.cliente;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class TesteSocketCLiente {

    public static void main(final String[] args) throws AWTException, IOException {
        final Robot robot = new Robot();
        final Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        final BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(screenFullImage, "jpg", outputStream);
        outputStream.flush();
        final byte[] sendData = outputStream.toByteArray();
        outputStream.close();

        final String SERVER_IP = "127.0.0.1";
        final int SERVER_PORT = 9876;

        try (final DatagramSocket clientSocket = new DatagramSocket()) {
            final InetAddress IPAddress = InetAddress.getByName(SERVER_IP);

            // Cálculo do tamanho do buffer considerando o tamanho máximo do pacote UDP
            final int BUFFER_SIZE = 1024;
            byte[] buffer;

            for (int i = 0; i < sendData.length; i += BUFFER_SIZE) {
                int chunkSize = Math.min(sendData.length - i, BUFFER_SIZE);
                buffer = Arrays.copyOfRange(sendData, i, i + chunkSize);

                final DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, IPAddress, SERVER_PORT);
                clientSocket.send(sendPacket);
            }
            final DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, IPAddress, SERVER_PORT);
            clientSocket.send(sendPacket);

            System.out.println("Screenshot enviado para o servidor em " + ((sendData.length + BUFFER_SIZE - 1) / BUFFER_SIZE) + " pacotes.");
        }
    }
}
