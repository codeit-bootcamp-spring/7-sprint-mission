package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBinaryContentCreated(BinaryContentCreatedEvent event) {
        log.debug("BinaryContent 업로드 시작 - id: {}", event.binaryContentId());

        try {
            binaryContentStorage.put(event.binaryContentId(), event.bytes());

            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.SUCCESS);
            log.info("BinaryContent 업로드 완료 - id: {}", event.binaryContentId());

        } catch (Exception e) {
            log.info("BinaryContent 업로드 실패 - id: {}", event.binaryContentId());
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.FAIL);
        }
    }
}
