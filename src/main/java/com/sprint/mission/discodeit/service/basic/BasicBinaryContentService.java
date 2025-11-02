package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void create(CreateBinaryContentRequestDto request) {
        BinaryContent bc = new BinaryContent(request.getContent());
        binaryContentRepository.save(bc);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new IllegalStateException("이미지 또는 파일이 존재하지 않습니다."));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAll().stream().
                filter(bc -> binaryContentIds.contains(bc.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID binaryContentId) {
        binaryContentRepository.delete(binaryContentId);
    }
}
