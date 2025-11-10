package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.service.ParticipationManagementService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipationManagementServiceImpl implements ParticipationManagementService {
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;



}
