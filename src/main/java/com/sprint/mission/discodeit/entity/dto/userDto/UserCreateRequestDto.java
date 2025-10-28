package com.sprint.mission.discodeit.entity.dto.userDto;

import lombok.*;

@Getter @ToString
@AllArgsConstructor
@Builder
public class UserCreateRequestDto {

    @NonNull
    private final String email;
    @NonNull
    private final String userName;
    @NonNull
    private final String password;
    private String phoneNum;    // 선택사항

    // 선택적으로 프로필 이미지 등록
    private byte[] profileImage;
    private String profileName;
    private String profileType;

}
