package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.event.BinaryContentUploadFailedEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;


@Slf4j
@Component

@ConditionalOnProperty(
        prefix = "discodeit.storage",
        name = "type",
        havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {
    private final Path root;
    private final ApplicationEventPublisher eventPublisher;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath, ApplicationEventPublisher eventPublisher) {
        this.root = Paths.get(rootPath);
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() {
        if (Files.notExists(root)) {
            try {
                Files.createDirectories(root);
                log.info("로컬 스토리지 디렉토리 생성 root={}", root.toAbsolutePath());
            } catch (IOException e) {
                log.error("로컬 스토리지 초기화 실패 root={}", root.toAbsolutePath(), e);
                throw new RuntimeException("로컬 스토리지 초기화 실패: " + root.toAbsolutePath(), e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public UUID put(UUID binaryId, byte[] bytes) {
        Path path = resolvePath(binaryId);
        try {
            Thread.sleep(3000);
            log.debug("binary content 저장 시도 id={} path={} size={}",
                    binaryId,
                    path,
                    bytes != null ? bytes.length : null
            );
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.debug("binary content 저장 성공 id={} path={}", binaryId, path);
            return binaryId;
        } catch (IOException e) {
            log.error("binary content 저장 실패 id={} path={}", binaryId, path, e);
            throw new RuntimeException("파일 저장 실패: " + binaryId, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while simulating delay", e);
        }
    }

    @Recover
    public UUID recover(RuntimeException e, UUID binaryId, byte[] bytes) {
        log.error("local 파일 업로드 최종 실패 message={}, binaryId={}", e.getMessage(), binaryId);
        String requestId = MDC.get("requestId");
        String error = e.getMessage();

        eventPublisher.publishEvent(
                new BinaryContentUploadFailedEvent(
                        requestId,
                        binaryId,
                        error
                )
        );

        throw new RuntimeException("로컬 업로드 최종 실패 binaryId=" + binaryId, e);
    }

    @Override
    public InputStream get(UUID binaryId) {
        try {
            return Files.newInputStream(resolvePath(binaryId), StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: " + binaryId, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentResponseDto binaryContentResponseDto) {
        try {
            log.debug("binary content 다운로드 시작 binaryId={} fileName={} size={}",
                    binaryContentResponseDto.id(),
                    binaryContentResponseDto.fileName(),
                    binaryContentResponseDto.size()
            );

            InputStream in = get(binaryContentResponseDto.id());
            ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

            MediaType mediaType = binaryContentResponseDto.contentType() != null
                    ? MediaType.parseMediaType(binaryContentResponseDto.contentType())
                    : MediaType.APPLICATION_OCTET_STREAM;

            log.debug("binary content 다운로드 응답 생성 id={}", binaryContentResponseDto.id());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + binaryContentResponseDto.fileName() + "\"")
                    .contentLength(binaryContentResponseDto.size() != null ? binaryContentResponseDto.size() : resource.contentLength())
                    .body(resource);

        } catch (IOException e) {
            log.error("binary content 다운로드 실패 binaryId={} fileName={}", binaryContentResponseDto.id(), binaryContentResponseDto.fileName(), e);
            throw new RuntimeException("다운로드 중 오류: " + binaryContentResponseDto.id(), e);
        }
    }
}
