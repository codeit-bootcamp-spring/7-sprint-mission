package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.config.enums.ContentOwner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService extends BaseService<BinaryContent, UUID> {
    BinaryContentDTO uploadFile(UUID ownerId, ContentOwner owner, MultipartFile multipartFile) throws IOException;

    List<BinaryContentDTO> findAllByOwnerId(UUID ownerId);
    List<BinaryContentDTO> findAllByFilePath(String filePath);

    void deleteAllByOwnerId(UUID ownerId);
}
