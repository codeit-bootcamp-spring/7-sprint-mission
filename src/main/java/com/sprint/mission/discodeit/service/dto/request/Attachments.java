package com.sprint.mission.discodeit.service.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record Attachments(
        List<MultipartFile> files
) {
}
