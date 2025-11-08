package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.transactional.CustomTransactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChannelDeleteFacade {
    private final ChannelService channelService;
    private final MessageService messageService;
    private final ReadStatusService readStatusService;

    @CustomTransactional
    public void deleteChannel(@NonNull UUID channelId){
        readStatusService.findAllByChannelId(channelId)
                .forEach(readStatus -> readStatusService.delete(readStatus.getId()));
        messageService.findAllByChannelId(channelId)
                .forEach(message -> messageService.delete(message.getId()));
        channelService.delete(channelId);
    }
}
