package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.status.BinaryContentStatus;
import com.sprint.mission.discodeit.event.binaryContent.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final ApplicationEventPublisher eventPublisher;
//    private final S3PrivateFileService s3PrivateFileService;

    @Override
    @Transactional
    public UUID uploadBinaryContent(BinaryContentUploadCommand command) {

        BinaryContent binaryContent = new BinaryContent(
                command.fileName(),
                command.contentType(),
                command.size(),
                BinaryContentStatus.PROCESSING
        );

        BinaryContent saved = binaryContentRepository.save(binaryContent);

        BinaryContentCreatedEvent binaryContentCreatedEvent = new BinaryContentCreatedEvent(saved.getId(), command.bytes(), command.userId());
        eventPublisher.publishEvent(binaryContentCreatedEvent);

        // 키id로 값 bytes 저장
        return saved.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentResponseDto getBinaryContent(UUID id) {
        log.debug("binary content 조회 시도 id={}", id);
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(() -> new BinaryContentNotFoundException(id));
        log.debug("binary content 조회 성공 binaryId={}", binaryContent.getId()); // NOTE: read는 너무많은 info 발생하므로 debug로
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResponseDto> getBinaryContentsByIds(List<UUID> ids) {
        List<BinaryContent> allByIds = binaryContentRepository.findAllById(ids);
        return allByIds.stream()
                .map(binaryContentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BinaryContentResponseDto updateStatus(UUID binaryContentId, BinaryContentStatus status) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new BinaryContentNotFoundException(binaryContentId));
        binaryContent.updateStatus(status);
        log.info("상태 변경 서비스 실행완료 binaryContentId={} status={}", binaryContentId, status);
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    @Transactional
    public void deleteBinaryContent(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }
        binaryContentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResponseDto> getAllBinaryContents() {
        log.debug("전체 binary content 조회 시도");
        List<BinaryContent> all = binaryContentRepository.findAll();
        log.debug("전체 binary content 조회 성공 count={}", all.size());
        return all.stream()
                .map(binaryContentMapper::toDto)
                .toList();
    }
}
