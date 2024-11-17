package br.com.carshop.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService() {
        this.root = getUploadDir();
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar a pasta para upload!", e);
        }
    }

    protected Path getUploadDir() {
        return Paths.get("uploads");
    }

    public String save(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Arquivo vazio");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Arquivo não é uma imagem válida");
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Files.copy(file.getInputStream(), this.root.resolve(newFilename));

            return "/uploads/" + newFilename;

        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar o arquivo. Erro: " + e.getMessage(), e);
        }
    }

    public void delete(String filename) {
        try {
            if (filename != null && filename.startsWith("/uploads/")) {
                String justFilename = filename.substring("/uploads/".length());
                Path file = root.resolve(justFilename);
                Files.deleteIfExists(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar arquivo: " + e.getMessage(), e);
        }
    }
}