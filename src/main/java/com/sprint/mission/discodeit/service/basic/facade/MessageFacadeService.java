package com.sprint.mission.discodeit.service.basic.facade;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageFacadeService {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public Message createMessage(CreateMessageDto dto) {
        User user = userService.getUser(dto.getUserId());
        Channel channel = channelService.getChannel(dto.getChannelID());
        Message message = messageService.createMessage(dto);

        userService.addChannelToUser(user, channel);
        channelService.addUserToChannel(channel, user);

        return message;
    }



}
