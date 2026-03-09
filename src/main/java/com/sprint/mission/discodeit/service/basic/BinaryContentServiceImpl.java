package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.exception.binaryContent.FileNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final SseService sseService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public BinaryContentDto createBinaryContent(BinaryContentCreateRequest requestDto) {

        BinaryContent newContent = BinaryContent.builder()
                .fileName(requestDto.dataName())
                .size((long) requestDto.data().length)
                .contentType(requestDto.dataType())
                .build();

        binaryContentRepository.save(newContent);
        String requestId = MDC.get("requestId");
        eventPublisher.publishEvent(new BinaryContentCreatedEvent(newContent.getId(), requestDto.data(), requestId));
        return binaryContentMapper.toDto(newContent);
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto findBinaryContentById(UUID id) {
        BinaryContent content=  binaryContentRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));
        return binaryContentMapper.toDto(content);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentDto> findAllBinaryContentByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
                .map(binaryContentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BinaryContentDto updateStatus(UUID id, BinaryContentStatus status) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));
        binaryContent.updateStatus(status);

        BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);

        sseService.broadcast("binaryContents.updated", dto);

        return dto;
    }

    @Override
    @Transactional
    public void deleteBinaryContentById(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
