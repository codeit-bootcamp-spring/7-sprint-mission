package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.BinaryContentStatusUpdatedEvent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        BinaryContent binary = new BinaryContent(
                request.fileName(),
                (long) request.bytes().length,
                request.contentType()
        );

        binaryContentRepository.save(binary);
        eventPublisher.publishEvent(new BinaryContentCreatedEvent(binary.getId(), request.bytes()));

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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BinaryContentDto updateStatus(UUID binaryContentId, BinaryContentStatus status) {
        BinaryContent binary = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new IllegalArgumentException("BinaryContent not found"));
        binary.updateStatus(status);
        BinaryContentDto dto = BinaryContentDto.from(binary);
        eventPublisher.publishEvent(new BinaryContentStatusUpdatedEvent(dto));
        return dto;
    }

    @Override
    public void delete(UUID binaryContentId) {
        binaryContentRepository.deleteById(binaryContentId);
    }
}