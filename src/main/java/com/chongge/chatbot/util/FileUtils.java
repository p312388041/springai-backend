package com.chongge.chatbot.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;

public class FileUtils {

    public static void classifyPdfFiles(String directoryPath) throws IOException {
        Files.walk(Path.of(directoryPath))
            .filter(Files::isRegularFile)
            .forEach(path -> {
                var reader = new TikaDocumentReader(new FileSystemResource(path));
                var document = reader.read().getFirst();
                if (document.getText() == null || document.getText().length() < 10000) {
                    final String target = directoryPath + "\\OCR\\" + path.getFileName();
                    try {
                        System.out.println("sources path:   " + path);
                        System.out.println("target:   " + target);
                        Files.move(path, Path.of(target));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
    }
}
