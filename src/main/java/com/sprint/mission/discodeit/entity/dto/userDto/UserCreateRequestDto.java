package com.sprint.mission.discodeit.entity.dto.userDto;

import lombok.*;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestDto {

    private String email;
    private String userName;
    private String password;
    private String phoneNum;    // 선택사항

    // 선택적으로 프로필 이미지 등록
    private byte[] profileImage;
    private String profileName;
    private String profileType;

}
