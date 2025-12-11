package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentResponseDto createBinaryContent(CreateBinaryContentRequestDto request);

  BinaryContentResponseDto find(UUID binaryContentId);

  List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds);

  void delete(UUID BinaryContentId);
}
