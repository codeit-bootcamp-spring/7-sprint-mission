package com.sprint.mission.discodeit.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserCreateRequestDto {

    private String name;
    private String userName;
    private String email;
    private String password;

}
