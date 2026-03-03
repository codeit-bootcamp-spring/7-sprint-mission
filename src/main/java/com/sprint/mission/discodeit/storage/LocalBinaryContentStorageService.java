package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.domain.file.FileDirectoryCreateFailException;
import com.sprint.mission.discodeit.exception.domain.file.FileReadFailException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

@Slf4j
@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorageService implements BinaryContentStorage {

    @Value( "${discodeit.storage.local.root-path}")
    private String root;

        @Override
    public UUID put(UUID id, byte[] bytes)  {
            Path storagePath = resolvePath(id);
            try {
                Thread.sleep(3000);
                Files.write(storagePath,bytes);
            } catch (IOException e) {
                throw new FileReadFailException(storagePath);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted while simulating delay", e);
            }
            log.debug("file Path : {}",storagePath);
        return id;
    }

    @Override
    public InputStream get(UUID fileId)  {
            Path storagePath = resolvePath(fileId);
            if(!Files.exists(storagePath))return null;

        try {
            return new ByteArrayInputStream(Files.readAllBytes(storagePath));
        } catch (IOException e) {
            throw new FileReadFailException(storagePath);
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto)  {
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
    void init()  {
        Path tempPath = Path.of(root).toAbsolutePath().normalize();
            if(!Files.exists(tempPath)) {
                try {
                    Files.createDirectories(tempPath);
                } catch (IOException e) {
                    throw new FileDirectoryCreateFailException(tempPath);
                }
            }
    }

    Path resolvePath(UUID fileId){

            Path tempPath = Path.of(root).toAbsolutePath().normalize();
            return tempPath.resolve(fileId.toString());
        }

}
