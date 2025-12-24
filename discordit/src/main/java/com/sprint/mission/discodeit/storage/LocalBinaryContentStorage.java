package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentAlreadyExistException;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
@ConditionalOnProperty(
        name = "discodeit.storage.type",
        havingValue = "local"
)
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {
    private final BinaryContentRepository binaryContentRepository;
    private final Path fileDir;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.file-path}") String fileDir, BinaryContentRepository binaryContentRepository) {
        this.fileDir = Path.of(fileDir);
        this.binaryContentRepository = binaryContentRepository;
    }

    @PostConstruct
    void init() {
        log.debug("Local BinaryContent Storage 초기화 시작");
        try {
            Files.createDirectories(fileDir);

            if (!Files.isWritable(fileDir)) {
                throw new IOException(fileDir + "의 쓰기 권한이 없습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("Local BinaryContent Storage 초기화 완료");
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] content) {
        Path path = resolvePath(binaryContentId);
        if (Files.exists(path)) {
            throw new BinaryContentAlreadyExistException(binaryContentId);
        }
        try {
            Files.write(path, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        if (Files.notExists(resolvePath(binaryContentId))) {
            throw new BinaryContentNotFoundException(binaryContentId);
        }
        try {
            return Files.newInputStream(resolvePath(binaryContentId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto dto) {
        BinaryContent binaryContent = binaryContentRepository.findById(dto.id())
                .orElseThrow(() -> new BinaryContentNotFoundException(dto.id()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + binaryContent.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
                .contentLength(binaryContent.getSize())
                .body(new InputStreamResource(get(binaryContent.getId())));
    }

    /**
     * 파일의 실제 저장 위치에 대한 규칙을 정의함
     * @param uuid 파일의 uuid
     * @return 그 파일이 저장될 Path
     */
    public Path resolvePath(UUID uuid) {
        return fileDir.resolve(uuid.toString());
    }
}
