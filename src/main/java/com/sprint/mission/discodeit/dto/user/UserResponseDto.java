package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
public class UserResponseDto {
    private UUID userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String pronoun;
    private List<Channel> joinChannels;
    private UUID profileId;
    private UserStatusType statusType;

    public static UserResponseDto from(User user, UserStatusType userStatusType) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .pronoun(user.getPronoun())
                .profileId(user.getProfileId())
                .statusType(userStatusType)
                .joinChannels(user.getJoinChannels())
                .build();
    }
}
