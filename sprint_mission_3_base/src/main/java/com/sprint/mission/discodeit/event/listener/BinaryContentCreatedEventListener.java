package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.scheduling.annotation.Async;

@Component
public class BinaryContentCreatedEventListener {


    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    public BinaryContentCreatedEventListener(BinaryContentStorage binaryContentStorage, BinaryContentService binaryContentService) {
        this.binaryContentStorage = binaryContentStorage;
        this.binaryContentService = binaryContentService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(BinaryContentCreatedEvent event) {
        try {
            binaryContentStorage.put(event.binaryContentId(), event.bytes());
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.SUCCESS);
        } catch (Exception e) {
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.FAIL);
            throw e;
        }
    }
}