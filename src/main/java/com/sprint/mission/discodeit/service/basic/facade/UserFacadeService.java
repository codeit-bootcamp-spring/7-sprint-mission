package com.sprint.mission.discodeit.service.basic.facade;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFacadeService {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public void removeUser(UUID userId) {
        // 1. 유저 조회
        User user = userService.getUser(userId);
        for (Channel channel : user.getJoinChannels()) {
            channelService.removeUserFromChannel(channel, user);
        }
        messageService.deleteMessagesByUser(userId);
        userService.deleteUser(userId);

    }
}
