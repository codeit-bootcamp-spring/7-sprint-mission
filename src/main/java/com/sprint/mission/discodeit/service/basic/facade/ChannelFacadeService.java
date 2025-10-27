package com.sprint.mission.discodeit.service.basic.facade;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelFacadeService {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public void addChannel(User user, Channel channel) {
        if (!userService.isExistsUser(user.getId())) {
            throw new NoSuchElementException("찾을 수 없는 유저: " + user.getId());
        }
        if (!channelService.isExistsChannel(channel.getId())) {
            throw new NoSuchElementException("찾을 수 없는 채널: " + channel.getId());
        }

        userService.addChannelToUser(user, channel);
        channelService.addUserToChannel(channel, user);
    }

    public void deleteChannel(Channel channel) {
        channelService.deleteChannel(channel.getId());
        userService.removeChannelFromAllUsers(channel);
        messageService.deleteMessagesByChannel(channel.getId());
    }
}
