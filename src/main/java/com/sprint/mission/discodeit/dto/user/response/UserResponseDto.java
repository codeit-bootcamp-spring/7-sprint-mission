package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.Common;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID userId,
        String username,
        String email,
        String phoneNumber,
        String pronoun,
        List<UUID> joinChannelIds,
        UUID profileId,
        UserStatusType statusType
) {
    public static UserResponseDto from(User user, UserStatusType userStatusType) {
        List<UUID> joinChannelIds = user.getJoinChannels().stream()
                .map(Common::getId)
                .toList();

        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .pronoun(user.getPronoun())
                .profileId(user.getProfileId())
                .statusType(userStatusType)
                .joinChannelIds(joinChannelIds)
                .build();
    }
}
