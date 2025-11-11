package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.binaryContent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    public BinaryContentResponseDto createBinaryContent(BinaryContentCreateRequestDto binaryContentCreateRequestDto);
    public BinaryContent find(UUID binaryContentID);
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIdList);
    public void deleteBinaryContent(UUID binaryContentId);
    public List<BinaryContent> findAll();
    public void resetBinaryContentService();

}
