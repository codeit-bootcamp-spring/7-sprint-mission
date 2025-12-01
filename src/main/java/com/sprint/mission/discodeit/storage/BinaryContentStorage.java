package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID id, byte[] data);
    InputStream get(UUID id);
    ResponseEntity<Resource> download(BinaryContentResponseDto  binaryContentResponseDto);
    void delete(UUID id);
}
