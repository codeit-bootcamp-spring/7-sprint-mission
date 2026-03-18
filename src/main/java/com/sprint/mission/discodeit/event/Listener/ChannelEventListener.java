package com.sprint.mission.discodeit.event.Listener;

import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entityElement.ChannelType;
import com.sprint.mission.discodeit.event.ChannelUpdatedEvent;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor

public class ChannelEventListener {

    private final SseService  sseService;
    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleChannelUpdate(ChannelUpdatedEvent  event) {

        ChannelDto channelDto = event.channelDto();
        String eventName = event.eventName();
        if(channelDto.type()== ChannelType.PUBLIC) {
            sseService.broadcast(eventName, channelDto);
            return;
        }
        List<UUID> participants = channelDto.participants().stream()
                .map(UserDto::id)
                .toList();
        sseService.send(participants,eventName,channelDto);
        return;
    }
}
