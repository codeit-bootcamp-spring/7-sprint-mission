package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID binaryContentId, byte[] bytes) throws IOException;
    InputStream get(UUID binaryContentId);
    ResponseEntity<?> download(BinaryContent binaryContent);
}
