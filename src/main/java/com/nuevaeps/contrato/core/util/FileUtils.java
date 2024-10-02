package com.nuevaeps.contrato.core.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {

    private static final String UPLOADS_DIR = "uploads/";

    // Validar que el archivo sea un .txt válido
    public static void validarArchivoTxt(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe tener un nombre válido");
        }

        if (!originalFilename.endsWith(".txt")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe ser un archivo .txt");
        }
    }

    // Crear el directorio de carga si no existe
    public static void crearDirectorioDeCargaSiNoExiste() {
        Path uploadsPath = Paths.get(UPLOADS_DIR);
        if (!Files.exists(uploadsPath)) {
            try {
                Files.createDirectories(uploadsPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el directorio de carga");
            }
        }
    }

    // Generar un nombre único basado en el nombre original del archivo
    public static String generarNombreDeArchivoUnico(String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String baseName = originalFilename.replace(".txt", "");
        return baseName + "_" + timestamp + ".txt";
    }

    // Guardar el archivo en el servidor
    public static void guardarArchivo(MultipartFile file, String nombreArchivo) {
        Path filePath = Paths.get(UPLOADS_DIR, nombreArchivo);

        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo");
        }
    }
}