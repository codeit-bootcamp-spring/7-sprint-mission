package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentStorage {
    BinaryContent put(MultipartFile file, BinaryContent binaryContent);
    InputStream get(UUID bianryContentId);
    String download(UUID binaryContentId); // HTTP API로 다운로드 기능을 제공
}
