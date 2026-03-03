package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentUploadFailException;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinaryContentCreatedEventListener {
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;

    @TransactionalEventListener
    public void onBinaryContentCreated(BinaryContentCreatedEvent event) {
        BinaryContent content = event.content;
        log.info("파일 저장 이벤트 수신 : {}", content.getId());
        try {
            binaryContentStorage.put(content.getId(), event.file);
            binaryContentRepository.save(content);
            content.uploadSucceed();
            log.info("파일 저장 완료 : {}", content.getId());
        } catch (Exception e) {
            log.error("파일 저장 실패 : {}", content.getId());
            content.uploadFailed();
            throw new BinaryContentUploadFailException(content);
        }
    }

}
