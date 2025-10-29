package com.sprint.mission.discodeit.entity.binaryContent;

import com.sprint.mission.discodeit.entity.binaryContent.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.binaryContent.dto.BinaryContentInfoDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentInfoDto createBinaryContent(BinaryContentCreateDto createDto);
    Optional<BinaryContentInfoDto> findBinaryContentByUserId(UUID userId);
    List<BinaryContentInfoDto> findAllBinaryContentByIdIn(UUID userId);
    void deleteBinaryContentById(UUID Id);



}
