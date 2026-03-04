package com.sprint.mission.discodeit.event.lilstener;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.event.dto.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinaryContentCreatedEventListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void onBinaryContentCreated(BinaryContentCreatedEvent event) {
        BinaryContent content = event.content();
        log.info("파일 저장 이벤트 수신 : {}", content.getId());
        try {
            binaryContentStorage.put(content.getId(), event.file());
            binaryContentRepository.save(content);
            content.uploadSucceed();
            log.info("파일 저장 완료 : {}", content.getId());
        } catch (Exception e) {
            log.error("파일 저장 실패 : {}", content.getId());
            content.uploadFailed();
        }
    }

}
