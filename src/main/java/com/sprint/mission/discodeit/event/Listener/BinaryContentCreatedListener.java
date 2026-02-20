package com.sprint.mission.discodeit.event.Listener;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entityElement.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j

public class BinaryContentCreatedListener {


    private final BinaryContentStorage  binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @Async("binaryExecutor")
    public void handleBinaryContentCreated(BinaryContentCreatedEvent event) {

        log.info("[바이너리 데이터 저장 쓰레드] 파일 이름: {}",event.getFilename());
        try {
            binaryContentStorage.put(event.getBinaryContentId(), event.getBytes());
            BinaryContent binaryContent = binaryContentRepository.findById(event.getBinaryContentId()).orElseThrow();
            binaryContent.updateStatus(BinaryContentStatus.SUCCESS);
        }
        catch (Exception e) {
            BinaryContent binaryContent = binaryContentRepository.findById(event.getBinaryContentId()).orElseThrow();
            binaryContent.updateStatus(BinaryContentStatus.FAILURE);
            log.warn("[바이너리 데이터 저장 실패] 원인 :{}",e.getMessage());
        }

    }

}
