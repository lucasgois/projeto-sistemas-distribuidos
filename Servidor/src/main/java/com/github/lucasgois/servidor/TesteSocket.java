package com.github.lucasgois.servidor;

import java.io.ByteArrayOutputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
public class TesteSocket {

    public void teste() throws IOException {
        final int PORT = 9876;

        try (final DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("Servidor UDP escutando na porta " + PORT);

            // Supondo que não sabemos o tamanho da imagem de antemão
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            boolean receiving = true;

            while (receiving) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // Verifica se o pacote está vazio, o que poderia ser usado para indicar o fim da transmissão
                // Este é apenas um exemplo e precisa de uma maneira real de determinar o fim da transmissão
                if (receivePacket.getLength() == 0) {
                    receiving = false;
                    break;
                }

                // Escreve os dados recebidos no ByteArrayOutputStream
                byteArrayOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
            }

            // Após receber todos os pacotes, escreve a imagem completa
            Files.write(Path.of("image.jpg"), byteArrayOutputStream.toByteArray());
            System.out.println("Imagem recebida e salva como image.jpg");
        }
    }

}
