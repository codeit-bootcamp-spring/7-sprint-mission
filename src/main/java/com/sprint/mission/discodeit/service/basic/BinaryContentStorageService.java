package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.info("✅ Files.createDirectories");
    }

    @Override
    public BinaryContent put(MultipartFile file, BinaryContent binaryContent) {
        log.info("🩷 BinaryContent put");
        try {
            // DB 저장
            BinaryContent newBinaryContent = binaryContentRepository.save(binaryContent);

            Path filePath = root.resolve(newBinaryContent.getId().toString());
//            log.info("✅ " + filePath.toString());
//            log.info("✅ length = " + file.getBytes().length);

            // 파일 저장
            Files.write(filePath, file.getBytes());  // byte[] -> 실제 파일로 저장
        } catch (IOException e) {
            throw new RuntimeException("🚨파일 저장 실패 I: " + e);
        }

        return binaryContent;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        log.info("🩷 InputStream get");

        if (binaryContentId == null) {
            throw new IllegalArgumentException("binaryContentId is null");
        }

        Path filePath = root.resolve(binaryContentId.toString());

        if (!Files.exists(filePath)) {
            throw new NoSuchElementException("파일이 존재하지 않습니다: " + filePath);
        }

        try {
            return Files.newInputStream(filePath);   // InputStream 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: " + filePath, e);
        }
    }

    @Override
    public void download(UUID binaryContentId) {
        log.info("🩷 download");
        //??🚨🚨🚨🚨살려!
//        BinaryContent content = binaryContentRepository.findById(binaryContentId)
//            .orElseThrow(() -> new NoSuchElementException("🚨binaryContentRepository.findById = [" + binaryContentDto.id() + "]err "));
//
//        InputStream fileStream = get(binaryContentId);

//        try {
//            byte[] bytes = fileStream.readAllBytes(); // InputStream → byte[]
//            return new BinaryContentDto(
//                content.getId(),
//                content.getFileName(),
//                content.getContentType(),
//                content.getFileSize(),
//                bytes
//            );
//        } catch (IOException e) {
//            throw new RuntimeException("파일 읽기 실패", e);
//        }
    }
}
