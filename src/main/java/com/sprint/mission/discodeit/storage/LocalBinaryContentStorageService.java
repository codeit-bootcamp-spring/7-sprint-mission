package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class LocalBinaryContentStorageService implements BinaryContentStorage {
    @Value( "${discodeit.storage.local.root-path}")
    private String root;

        @Override
    public UUID put(UUID id, byte[] bytes) throws IOException {
            Path storagePath = resolvePath(id);
            Files.write(storagePath,bytes);
        log.debug("file Path : {}",storagePath);
        return id;
    }

    @Override
    public InputStream get(UUID fileId) throws IOException {
            Path storagePath = resolvePath(fileId);
            if(!Files.exists(storagePath))return null;
        return new ByteArrayInputStream(Files.readAllBytes(storagePath));
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) throws IOException {
        InputStream targetInput = get(binaryContentDto.id());
        String contentType = binaryContentDto.contentType();
        String fileName = binaryContentDto.fileName();
        Resource resource = new InputStreamResource(targetInput);
        String encodeFile = UriUtils.encode(fileName, StandardCharsets.UTF_8);
        log.info("download : {}", encodeFile);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + encodeFile)
                .header("Content-Type",contentType)
                .body(resource);
    }

    @PostConstruct
    void init() throws IOException {
        Path tempPath = Path.of(root);
            if(!Files.exists(tempPath))Files.createDirectories(tempPath);
    }

    Path resolvePath(UUID fileId){

            Path tempPath = Path.of(root);
            return tempPath.resolve(fileId.toString());
        }

}
