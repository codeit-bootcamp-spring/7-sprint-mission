package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.entity.ReceiveType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateMessageRequestDto {
    String content;
    UUID channelId;
    UUID authorId;
}
