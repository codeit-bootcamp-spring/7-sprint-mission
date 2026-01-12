package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.global.util.file.FileManager;
import jakarta.annotation.PostConstruct;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local", matchIfMissing = true)
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path}") Path root) {
        this.root = root;
    }

    // Bean 생성 직후 자동 호출
    @PostConstruct
    public void init() {
        FileManager.init(root);
    }

    public Path resolvePath(UUID binaryContentId) {
        return root.resolve(binaryContentId.toString());
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        Path path = resolvePath(binaryContentId);
        FileManager.write(path, bytes);
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        return FileManager.read(path);
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentResponseDto dto) {
        InputStream inputStream = get(dto.id());
        Resource resource = new InputStreamResource(inputStream);

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(dto.fileName(), StandardCharsets.UTF_8)  // UTF-8 안전 인코딩
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .header(HttpHeaders.CONTENT_TYPE, dto.contentType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(dto.size()))
                .body(resource);
    }
}
