package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentStorage {
    BinaryContent put(MultipartFile file, BinaryContent binaryContent);
    InputStream get(UUID bianryContentId);
    void download(UUID binaryContentId); // HTTP API로 다운로드 기능을 제공
}
