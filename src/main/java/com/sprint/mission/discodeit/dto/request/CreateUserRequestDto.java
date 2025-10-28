package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CreateUserRequestDto {
    private String userName;
    private String nickName;
    private String email;
    private String phoneNum;
    private String userId;
    private String password;
    private MultipartFile profileImage;
}
