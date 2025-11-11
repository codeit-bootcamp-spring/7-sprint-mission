package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.config.enums.Status;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;

import java.util.List;

public record UserDetailInfo(
    UserResponseDTO user,
    List<ChannelSummary> channels,
    Status status,
    int unreadDirectMessageCount,
    List<String> userProfileImagePaths
) {
}
