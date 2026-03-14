package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.status.BinaryContentStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentEventListener {

    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;
    private final ApplicationEventPublisher eventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Timed("job.binary.upload")
    @Async(value = "binaryExecutor")
    public void handleUploadBinaryContent(BinaryContentCreatedEvent event) {
        try {

            binaryContentStorage.put(event.getBinaryId(), event.getBytes());
            binaryContentService.updateStatus(event.getBinaryId(), BinaryContentStatus.SUCCESS);

            eventPublisher.publishEvent(
                    new BinaryContentUpdatedEvent(event.getBinaryId(), event.getUserId())
            );

            // 만약 presigned url을 사용한다면 굳이 DB에 url을 저장할 필요가 없다.
            // 파일명(객체 key)을 DB에 저장하고 데이터를 불러올 일이있다면 그때마다 presigned url을 얻어서 프론트에게 전달
//            String url = s3PrivateFileService.uploadToFolder(command, "users/profile/");// TODO: s3업로드이후 url 값에 대해서 기존 코드 말고 쓰고 해당 필드 컬럼 어떻게 할지 정할것

        } catch (Exception e) {
            log.error("파일 저장 파일 실패 id={}, message={}", event.getBinaryId(), e.getMessage());
            binaryContentService.updateStatus(event.getBinaryId(), BinaryContentStatus.FAIL);
            eventPublisher.publishEvent(
                    new BinaryContentUpdatedEvent(event.getBinaryId(), event.getUserId())
            );
        }

    }

}
