package com.sprint.mission.discodeit.service.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.dto_Neo.BinaryContentStorageErrorEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.MessageCreatedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.RoleUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProduceRequiredEventListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String MESSAGE_CREATED_TOPIC = "discodeit.message-created-event";
    private static final String ROLE_UPDATED_TOPIC = "discodeit.role-updated-event";
    private static final String S3_UPLOAD_FAILED_TOPIC = "discodeit.s3-upload-failed-event";

    /**
     * 메시지 생성 이벤트 → Kafka 발행
     */
    @Async("myAsync")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageCreatedEvent event) {
        publishEvent( MESSAGE_CREATED_TOPIC, event);
    }

    /**
     * 역할 변경 이벤트 → Kafka 발행
     */
    @Async("myAsync")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RoleUpdatedEvent event) {
        publishEvent( ROLE_UPDATED_TOPIC,  event);
    }

    /**
     * S3 업로드 실패 이벤트 → 즉시 발행 (트랜잭션 무관)
     */
    @Async("myAsync")
    @EventListener
    public void on(BinaryContentStorageErrorEvent event) { // S3UploadFailedEvent
        publishEvent( S3_UPLOAD_FAILED_TOPIC, event);
    }

    private void publishEvent(String topic, Object event) {

        try {
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(topic, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("🚨 Kafka send failed. topic={}", topic, ex);
                    } else {
                        log.info("✅ Kafka send success. topic={}, partition={}, offset={}",
                            topic,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    }
                });

        } catch (Exception e) {
            log.error("🚨 Event serialization failed. topic={}, event={}", topic, event, e);
        }
    }
}
