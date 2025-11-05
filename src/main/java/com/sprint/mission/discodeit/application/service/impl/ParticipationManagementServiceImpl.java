package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.dto.SimpleChannelDTO;
import com.sprint.mission.discodeit.application.service.ParticipationManagementService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.channel.dto.ChannelMSGResponseDTO;
import com.sprint.mission.discodeit.participation.Participation;
import com.sprint.mission.discodeit.participation.ParticipationDualKey;
import com.sprint.mission.discodeit.participation.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipationManagementServiceImpl implements ParticipationManagementService {
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;



}
