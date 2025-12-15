package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage storage;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {

        BinaryContent binary = BinaryContent.builder()
                .fileName(request.fileName())
                .size((long) request.bytes().length)
                .contentType(request.contentType())
                .build();

        binaryContentRepository.save(binary);
        storage.put(binary.getId(), request.bytes());

        return BinaryContentDto.from(binary);
    }

    @Override
    public BinaryContentDto find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .map(BinaryContentDto::from)
                .orElseThrow(() -> new IllegalArgumentException("BinaryContent not found"));
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds).stream()
                .map(BinaryContentDto::from)
                .toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        binaryContentRepository.deleteById(binaryContentId);
    }
}
