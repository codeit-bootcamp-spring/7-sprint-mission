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
    public void createBinaryContent(CreateBinaryContentRequestDto request) {
        BinaryContent bc = new BinaryContent(request.getContent());
        binaryContentRepository.save(bc);
    }

    @Override
    public BinaryContent findBinaryContent(UUID id) {
        return binaryContentRepository.findById(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return binaryContentRepository.findAll().stream().
                filter(bc -> idList.contains(bc.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContent(UUID id) {
        binaryContentRepository.delete(id);
    }
}
