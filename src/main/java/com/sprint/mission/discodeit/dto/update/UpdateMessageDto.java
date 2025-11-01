package com.sprint.mission.discodeit.dto.update;

import java.util.UUID;

public record UpdateMessageDto (

    String newContent,
    UUID messageId
) {}
