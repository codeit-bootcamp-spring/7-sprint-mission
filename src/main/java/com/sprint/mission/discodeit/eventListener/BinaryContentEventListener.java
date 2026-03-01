package com.sprint.mission.discodeit.eventListener;

import com.sprint.mission.discodeit.dto.dto_Neo.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @Async("myAsync")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Observed(name = "message.create.async")
    public void handleBinaryContent(BinaryContentCreatedEvent event) {
        binaryContentStorage.put(event);
    }
}
