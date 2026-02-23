package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.exception.binaryContent.DirectoryCreationFailed;
import com.sprint.mission.discodeit.exception.binaryContent.FileOperationFailedException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {

    @Value("${discodeit.storage.local.root-path}")
    private String rootPath;

    private Path root;

    @PostConstruct
    void init() {
        log.info("Initializing local binary content storage");
        this.root = Paths.get(rootPath).toAbsolutePath();
        if (!Files.exists(this.root)) {
            try {
                Files.createDirectories(this.root);
            } catch (IOException e) {
                log.error("디렉토리 생성 실패: {}", e.getMessage());
                throw new DirectoryCreationFailed();
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
            Thread.sleep(3000);
            Files.write(path, bytes);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while simulating delay", e);
        } catch (IOException e) {
            throw new FileOperationFailedException(id);
        }
    }

    @Override
    public InputStream get(UUID id) {
        log.info("파일 읽기 시도");
        Path path = resolvePath(id);
        try {
            if (!Files.exists(path)) {
                log.warn("파일 읽기 실패 - 파일이 존재하지 않음: {}", id);
                throw new FileNotFoundException("파일을 찾을 수 없음");
            }
            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            log.warn("파일 스트림 생성 실패: {}", id);
            throw new FileOperationFailedException(id);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try {
            InputStream fileInputStream = get(binaryContentDto.id());
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            log.info("파일 다운로드 시작 - id: {}, fileName: {}", binaryContentDto.id(), binaryContentDto.fileName());

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
            log.warn("파일 다운로드 실패 - id: {}, Error: {}", binaryContentDto.id(), e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    //https://medium.com/@AlexanderObregon/client-file-downloads-in-spring-boot-with-byte-streams-6ea8f6205bb4
    //https://atl.kr/dokuwiki/doku.php/파일_다운로드시_한글_파일명_처리
}
