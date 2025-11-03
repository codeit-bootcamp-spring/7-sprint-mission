package com.sprint.mission.discodeit.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CreateUserRequestDto {
    private String userName;
    private String nickName;
    private String email;
    private String phoneNum;
    private String loginId;
    private String password;
    private MultipartFile profileImage;
}
