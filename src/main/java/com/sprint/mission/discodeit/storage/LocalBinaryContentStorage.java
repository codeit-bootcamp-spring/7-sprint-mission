package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.common.FileReadFailedException;
import com.sprint.mission.discodeit.global.exception.common.FileSaveFailedException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage{

    private final Path root;

    public LocalBinaryContentStorage(Path root) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        File directory = new File(root.toString());

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) throws IOException {
        Path path = resolvePath(binaryContentId);

        Files.createDirectories(path.getParent());
        Files.write(path, bytes);

        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);

        try{
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new FileReadFailedException(
                    ErrorCode.FILE_READ_FAILED,
                    Map.of("binaryContentId", binaryContentId)
            );
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContent binaryContent) {

        log.debug("파일 다운로드: binaryContentId = {}", binaryContent.getId());

        byte[] bytes;

        try {
            bytes = get(binaryContent.getId()).readAllBytes();
        } catch (IOException e) {
            throw new FileReadFailedException(
                    ErrorCode.FILE_READ_FAILED,
                    Map.of("binaryContentId", binaryContent.getId())
            );
        }

        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
                .header("Content-Disposition", "attachment; filename=\"" + binaryContent.getFileName() + "\"")
                .body(resource);
    }

    public Path resolvePath(UUID binaryContentId) {
        return root.resolve(binaryContentId.toString());
    }
}
