package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.mapper.dto.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {
    private final BinaryContentStorage binaryContentStorage;

//    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBinaryContent(BinaryContentCreatedEvent event) {
        binaryContentStorage.put(event);
    }
}
