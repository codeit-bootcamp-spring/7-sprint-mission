package com.sprint.mission.discodeit.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateReq(
        String email,
        String username,
        String phoneNumber,
        String password
) {
}
