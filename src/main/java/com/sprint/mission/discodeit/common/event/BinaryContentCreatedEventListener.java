package com.sprint.mission.discodeit.common.event;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentCreatedEventListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BinaryContentCreatedEvent event) {
        try {
            binaryContentStorage.put(event.binaryContentId(), event.data());
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.SUCCESS);

            log.info("BinaryContent {} was successfully uploaded", event.binaryContentId());
        } catch (Exception e) {
            log.error("BinaryContent {} upload failed", event.binaryContentId(), e);
            try {
                binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.FAIL);
            } catch (Exception ex) {
                log.error("BinaryContent {} status update failed", event.binaryContentId(), ex);
            }
        }
    }
}
