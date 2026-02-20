package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.global.exception.discodietException.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public BinaryContentResponseDto createBinaryContent(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = BinaryContent.builder()
                .fileName(createBinaryContentDto.fileName())
                .size(createBinaryContentDto.size())
                .contentType(createBinaryContentDto.contentType())
                .build();

        binaryContentRepository.save(binaryContent);
        eventPublisher.publishEvent(new BinaryContentCreatedEvent(this, binaryContent.getId(), createBinaryContentDto.bytes()));

        return binaryContentMapper.toResponseDto(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentResponseDto getBinaryContent(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));

        return binaryContentMapper.toResponseDto(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentResponseDto> getAllBinaryContentByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds)
                .stream()
                .map(binaryContentMapper::toResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw BinaryContentNotFoundException.byId(binaryContentId);
        }

        binaryContentRepository.deleteById(binaryContentId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateBinaryContentStatus(UUID binaryContentId, BinaryContentStatus status) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));
        binaryContent.updateStatus(status);
        log.info("BinaryContent {} status updated to {}", binaryContentId, status);
    }
}
