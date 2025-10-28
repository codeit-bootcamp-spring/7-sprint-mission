package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private UUID userId; //유저 ID
    private String username; // 유저 이름
    private String nickName; // 유저 닉네임
    private String email; // 이메일
    private UUID profileId; // 프로필 ID
}
