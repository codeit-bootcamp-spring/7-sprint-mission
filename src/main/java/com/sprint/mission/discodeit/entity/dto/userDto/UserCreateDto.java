package com.sprint.mission.discodeit.entity.dto.userDto;

import lombok.*;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {

    private String email;
    private String userName;
    private String password;
    private String phoneNum;    // 선택

}
