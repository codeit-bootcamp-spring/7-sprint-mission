package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public interface BinaryContentStorage {
    UUID put(UUID uuid, byte[] content);

    InputStream get(String fileName);

    ResponseEntity<?> download(BinaryContentDto dto);

    void delete(String filename);
}
