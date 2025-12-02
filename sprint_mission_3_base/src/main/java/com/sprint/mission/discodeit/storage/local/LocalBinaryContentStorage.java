package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import java.util.UUID;

@Component
@RequiredArgsConstructor
//@Profile("local")       // 🔥 discodeit.storage.type=local 일 때만 활성
@Primary               // 기본 구현체로 사용
public class LocalBinaryContentStorage implements BinaryContentStorage {

    @Value("${discodeit.storage.local.root-path}")
    private String rootPath;

    private Path root;

    @PostConstruct
    public void init() {
        this.root = Paths.get(rootPath);

        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장소 디렉토리 생성 실패", e);
        }
    }

    private Path resolvePath(UUID id) {
        return this.root.resolve(id.toString());
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);

        try {
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        return id;
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto dto) {
        InputStream input = get(dto.id());
        Resource resource = new InputStreamResource(input);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dto.fileName() + "\"")
                .contentType(MediaType.parseMediaType(dto.contentType()))
                .body(resource);
    }
}
