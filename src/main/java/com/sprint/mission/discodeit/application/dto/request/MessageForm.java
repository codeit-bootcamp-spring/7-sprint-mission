package com.sprint.mission.discodeit.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record MessageForm (
        String content,
        UUID authorId,
        UUID channelId
){}
