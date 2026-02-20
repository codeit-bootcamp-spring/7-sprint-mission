package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.global.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.global.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void create(CreateBinaryContentRequestDto request) {
        if (Optional.ofNullable(request).isPresent()) {
            BinaryContent file = new BinaryContent(
                    request.fileName(),
                    request.size(),
                    request.contentType()
            );

            BinaryContent saved = binaryContentRepository.save(file);
            eventPublisher.publishEvent(new BinaryContentCreatedEvent(saved.getId(), request.bytes()));
        }
    }

    @Override
    public BinaryContentResponseDto find(UUID binaryContentId) {
        BinaryContent content = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new BinaryContentNotFoundException(
                        ErrorCode.BINARYCONTENT_NOT_FOUND,
                        Map.of("binaryContentId", binaryContentId)
                ));

        return binaryContentMapper.toResponseDto(content);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContent> contents = binaryContentRepository.findAll().stream().
                filter(content -> binaryContentIds.contains(content.getId()))
                .collect(Collectors.toList());

        return binaryContentMapper.toResponseDto(contents);
    }

    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new BinaryContentNotFoundException(
                        ErrorCode.BINARYCONTENT_NOT_FOUND,
                        Map.of("binaryContentId", binaryContentId)
                ));
        binaryContentRepository.deleteById(binaryContentId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BinaryContentResponseDto updateStatus(UUID binaryContentId, BinaryContentStatus newStatus) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new BinaryContentNotFoundException(
                        ErrorCode.BINARYCONTENT_NOT_FOUND,
                        Map.of("binaryContentId", binaryContentId)));

        binaryContent.updateStatus(newStatus);

        return binaryContentMapper.toResponseDto(binaryContent);
    }

    @Override
    @Transactional
    public BinaryContent download(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new BinaryContentNotFoundException(
                        ErrorCode.BINARYCONTENT_NOT_FOUND,
                        Map.of("binaryContentId", binaryContentId)
                ));
    }
}
