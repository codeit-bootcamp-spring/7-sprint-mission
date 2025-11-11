package com.sprint.mission.discodeit.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ProfileRequest(
        MultipartFile profileImage
) {
}
