package br.com.carshop;

import br.com.carshop.services.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private Path uploadDir;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        uploadDir = tempDir.resolve("uploads");
        Files.createDirectories(uploadDir);

        fileStorageService = new FileStorageService() {
            @Override
            protected Path getUploadDir() {
                return uploadDir;
            }
        };
    }

    @Test
    void save_WithValidImage_ShouldSaveAndReturnPath() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        String path = fileStorageService.save(file);

        assertThat(path).isNotNull();
        assertThat(path).startsWith("/uploads/");
        assertThat(path).endsWith(".jpg");

        Path savedFile = uploadDir.resolve(path.substring("/uploads/".length()));
        assertThat(Files.exists(savedFile)).isTrue();
    }

    @Test
    void save_WithEmptyFile_ShouldThrowException() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        assertThatThrownBy(() -> fileStorageService.save(emptyFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Arquivo vazio");
    }

    @Test
    void save_WithInvalidFileType_ShouldThrowException() {
        MockMultipartFile textFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "text content".getBytes()
        );

        assertThatThrownBy(() -> fileStorageService.save(textFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não é uma imagem válida");
    }

    @Test
    void delete_WithValidPath_ShouldDeleteFile() throws IOException {
        String filename = "test.jpg";
        Path testFile = uploadDir.resolve(filename);
        Files.write(testFile, "test content".getBytes());
        assertThat(Files.exists(testFile)).isTrue();

        fileStorageService.delete("/uploads/" + filename);

        assertThat(Files.exists(testFile)).isFalse();
    }

    @Test
    void delete_WithInvalidPath_ShouldNotThrowException() {
        fileStorageService.delete("/uploads/nonexistent.jpg");
    }
}