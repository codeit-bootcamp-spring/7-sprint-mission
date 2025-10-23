package com.sprint.mission.discodeit.common.manager;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

@Service
public class FileManager {

    private final Path rootDir = Paths.get("data/uploads"); // 루트 폴더

    public BinaryContent saveFile(UUID userId, MultipartFile file) throws IOException {
        // 1. 유저별 폴더 생성
        Path userDir = rootDir.resolve(userId.toString());
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        // 2. 저장할 파일 경로
        Path filePath = userDir.resolve(file.getOriginalFilename());

        // 3. InputStream을 이용해 파일 저장
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 4. BinaryContent 객체 생성
        return new BinaryContent(
                UUID.randomUUID(),
                Instant.now()
//                file.getOriginalFilename(),
//                file.getContentType(),
//                file.getSize(),
//                filePath
        );
    }
}
