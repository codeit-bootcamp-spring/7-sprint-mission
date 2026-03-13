package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.BinaryContentStatusUpdatedEvent;
import com.sprint.mission.discodeit.event.ChannelChangedEvent;
import com.sprint.mission.discodeit.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.event.UserChangedEvent;
import com.sprint.mission.discodeit.service.sse.SseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SseRequiredEventListener {

    private final SseService sseService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotification(NotificationCreatedEvent event) {
        sseService.send(List.of(event.receiverId()), "notifications.created", event.notification());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBinary(BinaryContentStatusUpdatedEvent event) {
        sseService.broadcast("binaryContents.updated", event.binaryContent());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChannel(ChannelChangedEvent event) {
        sseService.broadcast(event.eventName(), event.channel());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUser(UserChangedEvent event) {
        sseService.broadcast(event.eventName(), event.user());
    }
}