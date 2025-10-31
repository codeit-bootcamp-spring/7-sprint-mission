package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChannelDeleteFacade {
    private final ChannelService channelService;
    private final MessageService messageService;
    private final ReadStatusService readStatusService;

    public void deleteChannel(UUID channelId){
        readStatusService.findAllByChannelId(channelId)
                .forEach(readStatus -> readStatusService.delete(readStatus.getId()));
        messageService.findAllByChannel(channelId)
                .forEach(message -> messageService.delete(message.getId()));
        channelService.delete(channelId);
    }
}
