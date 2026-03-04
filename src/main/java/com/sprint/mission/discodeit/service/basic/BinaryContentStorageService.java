package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.dto_Neo.S3UploadFailedEvent;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.dto_Neo.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentStorageService implements BinaryContentStorage {

    private final BinaryContentsRepository binaryContentRepository;
    private final Path root = Paths.get(System.getProperty("user.dir"), "data", "binary-content");
    private final ApplicationEventPublisher eventPublisher;

    private final BinaryContentService binaryContentService;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(root);
        log.info("✅ Files.createDirectories ok!");
    }


    @Override
    @Retryable(
        retryFor = {UncheckedIOException.class},  // 이 예외가 터지면 재시도
        maxAttempts = 2,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void put(BinaryContentCreatedEvent event) {
        try {
            // 의도적 지연
//            Thread.sleep(3000);

            Path filePath = root.resolve(event.getBinaryContentId().toString());

            // 파일 저장
            Files.write(
                filePath,
                event.getFile().getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

            log.info("✅ 파일 저장 SUCCESS ⭕️- id = {}", event.getBinaryContentId());

            binaryContentService.updateStatus(event.getBinaryContentId(), BinaryContentStatus.SUCCESS);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Recover
    public void recover(UncheckedIOException e, BinaryContentCreatedEvent event) {
        log.error(
            "🚨파일 저장 최종 실패 - binaryContentId={}, fileName={}",
            event.getBinaryContentId(),
            event.getFile().getOriginalFilename(),
            e
        );

        binaryContentService.updateStatus(event.getBinaryContentId(), BinaryContentStatus.FAIL);

        log.debug("❎");
        eventPublisher.publishEvent(new S3UploadFailedEvent(
                                            event.getBinaryContentId(),
                                            "🚨Error: " + e.getMessage()));
    }

    @Override
    public InputStream get(UUID binaryContentId) {

        if (binaryContentId == null) {
            throw new DiscodeitException(ErrorCode.ILLEAGALARGUEMNTEXCEPTION, Map.of("binaryContentId", "null"));
        }

        Path filePath = root.resolve(binaryContentId.toString());

        if (!Files.exists(filePath)) {
            throw new NoSuchElementException("🚨 파일이 존재하지 않습니다: " + filePath);
        }

        try {
            log.info("✅ 파일 get - binaryContentId = {}", binaryContentId.toString());
            return Files.newInputStream(filePath);   // InputStream 반환
        } catch (IOException e) {
            throw new RuntimeException("🚨파일 읽기 실패: " + filePath, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(UUID binaryContentId) {
        try {
            Path filePath = root.resolve(binaryContentId.toString());

            BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.ILLEAGALARGUEMNTEXCEPTION,
                    Map.of("BinaryContent not found", binaryContentId.toString())));

            InputStream inputStream = get(binaryContentId);

            MediaType mediaType = MediaType.parseMediaType((null == binaryContent.getContentType()) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : binaryContent.getContentType());
            String fileName = (null == binaryContent.getFileName()) ? binaryContentId.toString() : binaryContent.getFileName();
            String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            log.info("✅ 파일 download! - binaryContentId = {}", binaryContentId.toString());

            return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encoded + "\"; filename*=UTF-8''" + encoded)
                .contentLength((null == binaryContent.getSize()) ? Files.size(filePath) : binaryContent.getSize())
                .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            throw new DiscodeitException(ErrorCode.FILE_NOT_FOUND,
                Map.of("binaryContentId", "null"));
        }
    }
}
