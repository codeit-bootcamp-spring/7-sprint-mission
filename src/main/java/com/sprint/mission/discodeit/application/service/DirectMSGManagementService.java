package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.application.dto.DirectMessageDetailInfoDTO;

import java.util.UUID;

public interface DirectMSGManagementService {
    DirectMessageDetailInfoDTO getDirectMessageDetailInfo(UUID directMessageId);
}
