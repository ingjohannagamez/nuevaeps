package com.nuevaeps.contrato.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

public class FileUtils {

    private static final String UPLOADS_DIR = "uploads/";

    // Valida si el archivo tiene nombre y extensión correctos
    public static void validateTxtFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe tener un nombre válido");
        }

        if (!originalFilename.endsWith(".txt")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe ser un archivo .txt");
        }
    }

    // Crea el directorio de carga si no existe
    public static void createUploadDirectoryIfNotExists() {
        Path uploadsPath = Paths.get(UPLOADS_DIR);
        if (!Files.exists(uploadsPath)) {
            try {
                Files.createDirectories(uploadsPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el directorio de carga");
            }
        }
    }

    // Genera un nombre único para el archivo basado en la fecha y hora
    public static String generateUniqueFileName(String originalFilename) {
        String fileNameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return fileNameWithoutExt + "_" + timestamp + ".txt";
    }

    // Guarda el archivo en el servidor
    public static void saveFile(MultipartFile file, String newFilename) {
        Path filePath = Paths.get(UPLOADS_DIR).resolve(newFilename);
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo");
        }
    }
}