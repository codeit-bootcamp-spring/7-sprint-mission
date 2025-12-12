package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage storage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public BinaryContentResponseDto createBinaryContent(CreateBinaryContentRequestDto request) {

    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        (long) request.bytes().length,
        request.contentType()

    );

    BinaryContent saved = binaryContentRepository.save(binaryContent);
    storage.put(binaryContent.getId(), request.bytes());

    return binaryContentMapper.toDto(saved);
  }

  @Override
  public BinaryContentResponseDto find(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new BinaryContentNotFoundException(binaryContentId));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .map(content -> binaryContentMapper.toDto(content))
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new BinaryContentNotFoundException(binaryContentId));
    binaryContentRepository.delete(binaryContent);
  }
}
