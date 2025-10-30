package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.service.AuthService;
import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final ParticipationService participationService;
    private final DirectMessageService directMessageService;
    private final ChannelService channelService;
    private final ChannelMessageService channelMessageService;


}
