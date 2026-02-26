package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

@Component
@ConditionalOnProperty(
        prefix = "discodeit.storage",
        name = "type",
        havingValue = "local"
)
public class LocalBinaryContentStorage implements  BinaryContentStorage{
    private final Path root;

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path}") String rootPath) {
        this.root = Paths.get(rootPath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("failed to initialize local storage" + root, e);
        }

    }

    Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    public UUID put(UUID id, byte[] data) {
        Path path = resolvePath(Objects.requireNonNull(id));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while simulating delay", e);
        }
        try {
            Files.write(path,
                    data,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to store binary content" + id, e);
        }
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(Objects.requireNonNull(id));

        try {
            return Files.newInputStream(path, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read local binary content" + id, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentResponseDto binaryContentResponseDto) {
        Path path = resolvePath(Objects.requireNonNull(binaryContentResponseDto.id()));

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("file does not exist: " + path);
        }

        try {
            InputStream inputStream = get(binaryContentResponseDto.id());
            InputStreamResource resource = new InputStreamResource(inputStream);

            String contentType = binaryContentResponseDto.contentType() != null ?
                    binaryContentResponseDto.contentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

            String fileName = binaryContentResponseDto.fileName() != null ?
                    binaryContentResponseDto.fileName() : binaryContentResponseDto.id().toString();

            String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    encoded + "\"; filename*=UTF-8''" + encoded)
                    .contentLength(binaryContentResponseDto.size() != null ?
                            binaryContentResponseDto.size() : Files.size(path))
                    .body(resource);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to download local binary content" + path, e);
        }
    }

    @Override
    public void delete(UUID id) {
        Path path = resolvePath(Objects.requireNonNull(id));
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to delete local binary content" + path, e);
        }
    }
}
