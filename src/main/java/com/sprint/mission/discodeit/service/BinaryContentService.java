package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.binaryContent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    public BinaryContentDto createBinaryContent(BinaryContentCreateRequestDto binaryContentCreateRequestDto) throws IOException;
    public BinaryContentDto find(UUID binaryContentID);
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIdList);
    public void deleteBinaryContent(UUID binaryContentId);
    public List<BinaryContent> findAll();
    public void resetBinaryContentService();
    ResponseEntity<?> downloadFile(UUID binaryContentId) throws IOException;

}
