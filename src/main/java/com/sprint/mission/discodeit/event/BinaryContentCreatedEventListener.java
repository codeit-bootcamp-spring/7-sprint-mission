package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.service.binarycontent.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentCreatedEventListener {

    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentStatusUpdater statusUpdater;

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BinaryContentCreatedEvent event) {
        try {
            binaryContentStorage.put(event.getFileName(), event.getFile());
            statusUpdater.markSuccess(event.getBinaryContentId());
        } catch (Exception e) {
            log.error("Binary content save failed. id={}", event.getBinaryContentId(), e);
            statusUpdater.markFail(event.getBinaryContentId());
        }
    }
}
