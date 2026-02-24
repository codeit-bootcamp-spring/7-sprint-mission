package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.dto.dto_Neo.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @Async("myAsync")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Observed(name = "message.create.async")
    public void handleBinaryContent(BinaryContentCreatedEvent event) {
        try {

            binaryContentStorage.put(event);
            binaryContentService.updateStatus(event.getBinaryContentId(), BinaryContentStatus.SUCCESS);
        } catch (Exception e) {
            binaryContentService.updateStatus(event.getBinaryContentId(), BinaryContentStatus.FAIL);
            throw new RuntimeException(e);
        }
    }
}
