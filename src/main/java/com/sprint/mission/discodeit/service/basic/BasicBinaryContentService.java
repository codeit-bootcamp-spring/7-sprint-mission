package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
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

  @Override
  @Transactional
  public BinaryContent createBinaryContent(CreateBinaryContentRequestDto request) {

    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        request.contentType(),
        request.bytes()
    );

    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new IllegalArgumentException("BinaryContent를 찾을 수 없습니다."));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds);
  }

  @Override
  @Transactional
  public void delete(UUID BinaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(BinaryContentId)
        .orElseThrow(() -> new IllegalArgumentException("BinaryContent를 찾을 수 없습니다."));
    binaryContentRepository.delete(binaryContent);
  }
}
