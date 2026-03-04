package com.sprint.mission.discodeit.global.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.global.exception.common.FileSaveFailedException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentCreatedEventListener {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

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
    }
}
