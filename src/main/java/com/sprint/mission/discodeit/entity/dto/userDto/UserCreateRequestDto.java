package com.sprint.mission.discodeit.entity.dto.userDto;

import lombok.*;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestDto {

    @NonNull
    private String email;
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private String phoneNum;    // 선택사항

    // 선택적으로 프로필 이미지 등록
    private byte[] profileImage;
    private String profileName;
    private String profileType;

}
