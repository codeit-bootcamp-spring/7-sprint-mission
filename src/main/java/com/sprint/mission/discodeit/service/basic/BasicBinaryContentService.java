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

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
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
  public List<BinaryContent> findAllByIdIn(List<UUID> BinaryContentIds) {
    List<BinaryContent> allByIdIn = binaryContentRepository.findAllByIdIn(BinaryContentIds);
    return allByIdIn;
  }

  @Override
  public void delete(UUID BinaryContentId) {
    binaryContentRepository.delete(BinaryContentId);
  }
}
