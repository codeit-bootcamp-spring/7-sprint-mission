package com.sprint.mission.discodeit.service.binaryContent;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LocalBinaryContentStorageService implements BinaryContentStorage {
    @Value( "${storage.root}")
    private Path root;

    private Map<UUID, byte[]> localStorage = new HashMap<>();
        @Override
    public UUID put(UUID id, byte[] bytes) throws IOException {
            Path storagePath = resolvePath(id);
            Files.write(storagePath,bytes);
        return id;
    }

    @Override
    public InputStream get(UUID fileId) {
        return localStorage.get(fileId) == null ? null : new ByteArrayInputStream(localStorage.get(fileId));
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
        return null;
    }

    @PostConstruct
    void init() throws IOException {
            if(!Files.exists(root))Files.createDirectories(root);
        Files.deleteIfExists(root);
    }

    Path resolvePath(UUID fileId){
            Path resultPath = root.resolve(fileId.toString());
            return resultPath;
        }

}
