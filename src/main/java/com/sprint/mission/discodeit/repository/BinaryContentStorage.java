package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

    public UUID put(UUID id, byte[] bytes) throws IOException;
    public InputStream get(UUID fileId) throws IOException;
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) throws IOException;
}
