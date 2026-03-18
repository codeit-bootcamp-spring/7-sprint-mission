package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketRequiredEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessage(MessageCreatedEvent event) {
        MessageResponseDto messageResponseDto = event.getMessageResponseDto();
        String channelId = messageResponseDto.channelId().toString();

        String destination = "/sub/channels." + channelId + ".messages";

        messagingTemplate.convertAndSend(destination, messageResponseDto);
        log.debug("WebSocket message sent to {}: {}", destination, messageResponseDto.id());
    }
}
