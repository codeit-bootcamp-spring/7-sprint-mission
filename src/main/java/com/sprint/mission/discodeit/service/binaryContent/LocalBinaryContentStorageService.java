package com.sprint.mission.discodeit.service.binaryContent;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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
import java.util.stream.Stream;

@Service
public class LocalBinaryContentStorageService implements BinaryContentStorage {
    @Value( "${storage.root}")
    private Path root;

        @Override
    public UUID put(UUID id, byte[] bytes) throws IOException {
            Path storagePath = resolvePath(id);
            Files.write(storagePath,bytes);
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
        InputStream targetInput =    get(binaryContentDto.id());
        Resource resource = new InputStreamResource(targetInput);

        return new ResponseEntity<>(resource,HttpStatus.OK);
    }

    @PostConstruct
    void init() throws IOException {
            if(!Files.exists(root))Files.createDirectories(root);
        Stream<Path> files = Files.list(root);
        files.forEach(x->
                {
                    try {
                        Files.deleteIfExists(x);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                );
    }

    Path resolvePath(UUID fileId){
        return root.resolve(fileId.toString());
        }

}
