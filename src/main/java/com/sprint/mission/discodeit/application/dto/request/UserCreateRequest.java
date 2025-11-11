package com.sprint.mission.discodeit.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserCreateRequest(
        String email,
        String password,
        String username,
        String phoneNumber

)
 {}