package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ChannelSseEventListener {

    private final ChannelService channelService;
    SseService sseService;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("eventTaskExecutor")
    public void handleChannelCreated(ChannelCreatedEvent event) {

        ChannelResponseDto responseDto = channelService.getChannel(event.getChannelId());

        if(responseDto.type().equals(ChannelType.PRIVATE)){
        sseService.send(
                responseDto.participants().stream().map(UserResponseDto::id).toList(),
                "channels.created",
                responseDto
        );
        }
    }
}
