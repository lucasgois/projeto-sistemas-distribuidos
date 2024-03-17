package com.github.lucasgois.cliente;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoChunker {
    public static void splitVideoIntoChunks(String videoFilePath, int chunkSizeMB) {
        File videoFile = new File(videoFilePath);
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(videoFile);
            byte[] buffer = new byte[chunkSizeMB * 1024 * 1024]; // Converte o tamanho do chunk para bytes
            int bytesRead;
            int partNumber = 1;

            while ((bytesRead = fis.read(buffer)) != -1) {
                // Cria um novo arquivo para cada chunk
                File newChunk = new File(videoFile.getParent(), "chunk" + partNumber + ".mp4");
                fos = new FileOutputStream(newChunk);

                fos.write(buffer, 0, bytesRead);
                fos.close();
                fos = null; // Garante que o FileOutputStream é resetado após o uso
                partNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Fecha os streams abertos
            try {
                if (fis != null) fis.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String videoFilePath = "Dive.mp4";
        int chunkSizeMB = 5; // Define o tamanho de cada chunk em MB
        splitVideoIntoChunks(videoFilePath, chunkSizeMB);
    }
}
