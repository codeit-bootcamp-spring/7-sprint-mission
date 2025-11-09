package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.Common;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID userId,
        Instant createAt,
        Instant updateAt,
        String username,
        String email,
        String phoneNumber,
        String pronoun,
        List<UUID> joinChannelIds,
        UUID profileId,
        Boolean online
) {
    public static UserResponseDto from(User user, UserStatusType userStatusType) {
        List<UUID> joinChannelIds = user.getJoinChannels().stream()
                .map(Common::getId)
                .toList();

        return UserResponseDto.builder()
                .userId(user.getId())
                .createAt(user.getCreateAt())
                .updateAt(Instant.now())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .pronoun(user.getPronoun())
                .profileId(user.getProfileId())
                .online(userStatusType.isOnline())
                .joinChannelIds(joinChannelIds)
                .build();
    }
}
