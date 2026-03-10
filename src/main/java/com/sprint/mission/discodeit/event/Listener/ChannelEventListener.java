package com.sprint.mission.discodeit.event.Listener;

import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
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

        Channel channel = channelRepository.findById(event.channelId()).orElseThrow();
        ChannelDto dto = channelMapper.toDto(channel);
        String eventName = event.eventName();
        if(event.type()== ChannelType.PUBLIC) {
            sseService.broadcast(eventName, dto);
            return;
        }
        List<UUID> participants = event.participants().stream()
                .map(x -> x.id())
                .toList();
        sseService.send(participants,eventName,dto);
        return;
    }
}
