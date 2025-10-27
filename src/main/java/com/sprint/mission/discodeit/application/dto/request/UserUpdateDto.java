package com.sprint.mission.discodeit.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserUpdateDto(
        UUID id,
        String email,
        String username,
        String phoneNumber,
        String password,
        MultipartFile updateFile
        //null이면 업데이트 하지 않는 걸로
) {
}
