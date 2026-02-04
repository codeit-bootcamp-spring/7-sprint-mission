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
    public UUID put(UUID uuid, byte[] content) {
        log.info("파일 저장 요청 들어옴 : {}", uuid);

        BinaryContent file = binaryContentRepository.findById(uuid)
                .orElseThrow(() -> new BinaryContentNotFoundException(uuid));

        Path path = fileDir.resolve(file.getFileName());
        if (Files.exists(path)) {
            log.error("동일한 파일 이름이 존재해 파일 저장 실패");
            throw new BinaryContentAlreadyExistException(uuid);
        }
        try {
            log.info("파일 저장 디렉터리가 없어서 생성함");
            Files.write(path, content);
        } catch (IOException e) {
            log.error("IOException 발생 : {}", e.getMessage());
            throw new RuntimeException(e);
        }
        log.debug("파일 생성 성공. 파일 이름 : {}", file.getFileName());
        return uuid;
    }

    @Override
    public InputStream get(String fileName) {
        log.info("파일 조회 요청 들어옴");
        if (Files.notExists(fileDir.resolve(fileName))) {
            log.error("존재하지 않는 파일 이름으로 조회 실패");
            throw new BinaryContentNotFoundException(fileName);
        }
        try {
            log.debug("파일 조회 성공");
            return Files.newInputStream(fileDir.resolve(fileName));
        } catch (IOException e) {
            log.error("IOException 발생 : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto dto) {
        log.info("파일 다운로드 요청 들어옴");
        BinaryContent binaryContent = binaryContentRepository.findById(dto.id())
                .orElseThrow(() -> new BinaryContentNotFoundException(dto.id()));
        log.debug("파일 다운로드 성공");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + binaryContent.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
                .contentLength(binaryContent.getSize())
                .body(new InputStreamResource(get(dto.fileName())));
    }

    @Override
    public void delete(String filename) {
        binaryContentRepository.deleteByFileName(filename);
    }
}
