package com.sprint.mission.discodeit.service.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ProfileRequest(
        MultipartFile profileImage
) {
}
