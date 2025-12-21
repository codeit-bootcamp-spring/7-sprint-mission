package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.BinaryContentException;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentStorageService implements BinaryContentStorage {
    private final BinaryContentsRepository binaryContentRepository;
    private final Path root = Paths.get(System.getProperty("user.dir"), "data", "binary-content");

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(root);
        log.info("✅ Files.createDirectories ok!");
    }

    @Override
    public BinaryContent put(MultipartFile file, BinaryContent binaryContent) {
        try {
            // DB 저장 -> id 찾기 위함. 실제 저장 안되니 put 후에 실제 DB 저장 필요!
            BinaryContent newBinaryContent = binaryContentRepository.save(binaryContent);
            Path filePath = root.resolve(newBinaryContent.getId().toString());

            // 파일 저장
            Files.write(filePath, file.getBytes());  // byte[] -> 실제 파일로 저장
        } catch (IOException e) {

            log.error("🚨파일 저장 실패 - file.name = {}", file.getOriginalFilename());
            throw new RuntimeException("🚨파일 저장 실패 I: " + e);
        }

        log.info("✅ 파일 저장 - file.name = {}", file.getOriginalFilename());
        return binaryContent;
    }

    @Override
    public InputStream get(UUID binaryContentId) {

        if (binaryContentId == null) {
            throw new DiscodeitException(ErrorCode.ILLEAGALARGUEMNTEXCEPTION, Map.of("binaryContentId", binaryContentId.toString()));
        }

        Path filePath = root.resolve(binaryContentId.toString());

        if (!Files.exists(filePath)) {
            log.error("🚨파일이 존재하지 않습니다: - filePath = {}", filePath);
            throw new NoSuchElementException("파일이 존재하지 않습니다: " + filePath);
        }

        try {
            log.info("✅ 파일 get - binaryContentId = {}", binaryContentId.toString());
            return Files.newInputStream(filePath);   // InputStream 반환
        } catch (IOException e) {
            log.error("🚨파일 읽기 실패! - filePath = {}", filePath);
            throw new RuntimeException("파일 읽기 실패: " + filePath, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(UUID binaryContentId) {
        try {
            Path filePath = root.resolve(binaryContentId.toString());

            BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElse(null);

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
            log.error("🚨download error! - binaryContentId = {}", binaryContentId.toString());
            throw new DiscodeitException(ErrorCode.ILLEAGALARGUEMNTEXCEPTION, Map.of("binaryContentId", binaryContentId.toString()));
        }
    }
}
