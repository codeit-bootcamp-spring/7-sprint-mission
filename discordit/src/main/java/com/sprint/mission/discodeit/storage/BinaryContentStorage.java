package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Path;

@Component
public interface BinaryContentStorage {
    Path put(String fileName, byte[] content);

    InputStream get(String fileName);

    ResponseEntity<?> download(BinaryContentDto dto);
}
