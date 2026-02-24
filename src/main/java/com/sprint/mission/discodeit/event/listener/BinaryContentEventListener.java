package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentEventListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @Async(value = "eventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBinaryContentCreated(BinaryContentCreatedEvent event) {
        UUID binaryContentId = event.getBinaryContentId();
        byte[] data = event.getData();
        try {
            binaryContentStorage.put(binaryContentId, data);
            binaryContentService.updateBinaryContentStatus(binaryContentId, BinaryContentStatus.SUCCESS);
            log.info("BinaryContent {} 업로드 성공", binaryContentId);
        } catch (Exception e) {
            binaryContentService.updateBinaryContentStatus(event.getBinaryContentId(), BinaryContentStatus.FAIL);
            log.info("BinaryContent {} 업로드 실패", binaryContentId);
        }
    }
}
