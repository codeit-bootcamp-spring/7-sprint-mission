package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.application.dto.DirectMessageDetailInfo;

import java.util.UUID;

public interface DirectMSGManagementService {
    DirectMessageDetailInfo getDirectMessageDetailInfo(UUID directMessageId);
}
