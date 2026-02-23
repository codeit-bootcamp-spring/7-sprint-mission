package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.mapper.dto.BinaryContentCreatedEvent;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {
    void put(BinaryContentCreatedEvent event);
    InputStream get(UUID bianryContentId);
    ResponseEntity<Resource> download(UUID binaryContentId); // HTTP API로 다운로드 기능을 제공
}
