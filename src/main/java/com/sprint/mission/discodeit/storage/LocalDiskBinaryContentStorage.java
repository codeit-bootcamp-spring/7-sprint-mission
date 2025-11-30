package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.NoSuchElementException;
import java.util.UUID;


@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local")
public class LocalDiskBinaryContentStorage implements BinaryContentStorage {


    @Value("${discodeit.storage.local.root-path:./data/binary}")
    private String rootPathConfig;

    private Path root;


    @PostConstruct
    public void init() {
        this.root = Paths.get(rootPathConfig).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
            System.out.println("[BinaryStorage] 경로생성 준비완료: " + root);
        } catch (IOException e) {
            throw new UncheckedIOException("경로생성에 실패했어요: " + root, e);
        }
    }


    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        try {
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return id;
        } catch (IOException e) {
            throw new UncheckedIOException("파질저장실패했습니다 경로: " + path, e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        try {
            if (!Files.exists(path)) {
                throw new NoSuchElementException("파일이 존재하지않아요: " + id);
            }
            return Files.newInputStream(path, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new UncheckedIOException("파일 접속실패: " + path, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto dto) {
        InputStreamResource resource = new InputStreamResource(get(dto.id()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(dto.fileName(), StandardCharsets.UTF_8)
                        .build()
        );

        MediaType mediaType = MediaType.parseMediaType(dto.contentType());


        headers.setContentType(mediaType);
        if (dto.size() != null && dto.size() > 0) headers.setContentLength(dto.size());

        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
