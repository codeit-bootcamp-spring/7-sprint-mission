package com.sprint.mission.discodeit.global.event;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.global.exception.common.FileSaveFailedException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.sse.service.SseService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentCreatedEventListener {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    private final BinaryContentMapper binaryContentMapper;
    private final SseService sseService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async("asyncTaskExecutor")
    public void handle(BinaryContentCreatedEvent event) {
        BinaryContent binaryContent = binaryContentRepository.findById(event.getBinaryContentId())
                .orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.BINARYCONTENT_NOT_FOUND));

        try {
            binaryContentStorage.put(
                    binaryContent.getId(),
                    event.getBytes()
            );

            binaryContent.updateStatus(BinaryContentStatus.SUCCESS);
            binaryContentRepository.save(binaryContent);
        } catch (IOException e) {
            binaryContent.updateStatus(BinaryContentStatus.FAIL);
            binaryContentRepository.save(binaryContent);

            throw new FileSaveFailedException(
                    ErrorCode.FILE_SAVE_FAILED,
                    Map.of("binaryContentId", binaryContent.getId())
            );
        }

        // 파일 업로드 상태 변경 이벤트 전송
        BinaryContentResponseDto dto = binaryContentMapper.toResponseDto(binaryContent);

        sseService.send(
                List.of(dto.id()),
                "binaryContents.updated",
                dto
        );
    }
}
