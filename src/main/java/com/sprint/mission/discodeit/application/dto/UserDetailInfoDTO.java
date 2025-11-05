package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.Status;
import com.sprint.mission.discodeit.content.binary.BinaryContent;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public record UserDetailInfoDTO(
    UserResponseDTO user,
    List<ChannelSummaryDTO> channels,
    Status status,
    int unreadDirectMessageCount,
    String userProfileImagePath
) {
}
