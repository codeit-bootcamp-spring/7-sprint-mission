package com.sprint.mission.discodeit.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record MessageUpdate(
        UUID id,
        String content,
        MultipartFile image
) {
}
