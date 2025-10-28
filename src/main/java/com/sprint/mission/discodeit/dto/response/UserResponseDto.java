package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private UUID userId;
    private String username; // 유저 이름
    private String nickName; // 유저 닉네임
    private String email; // 이메일
    private UUID profileId; // 프로필 ID
    private boolean isOnline; //온라인 상태

    public static UserResponseDto from(User user, UserStatus status){
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getEmail(),
                user.getProfileId(),
                status.isOnline()
        );
    }
}
