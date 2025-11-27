package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    @Value("${discodeit.storage.local.root-path}")
    private String rootPath;

    private Path root;

    @PostConstruct
    void init() {
        this.root = Paths.get(rootPath).toAbsolutePath();
        if (!Files.exists(this.root)) {
            try {
                Files.createDirectories(this.root);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패");
            }
        }
    }

    private Path resolvePath(UUID id) {
        return this.root.resolve(id.toString());
    }


    @Override
    public void put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        try {
            if (!Files.exists(path))
                throw new FileNotFoundException("파일을 찾을 수 없음");

            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기에 실패했습니다.");
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try {
            InputStream fileInputStream = get(binaryContentDto.id());
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(binaryContentDto.fileName(), StandardCharsets.UTF_8)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            headers.setContentLength(binaryContentDto.size());
            headers.setContentType(MediaType.parseMediaType(binaryContentDto.contentType()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //https://medium.com/@AlexanderObregon/client-file-downloads-in-spring-boot-with-byte-streams-6ea8f6205bb4
    //https://atl.kr/dokuwiki/doku.php/파일_다운로드시_한글_파일명_처리
}
