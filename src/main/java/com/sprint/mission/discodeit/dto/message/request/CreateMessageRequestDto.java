package com.sprint.mission.discodeit.dto.message.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReceiveType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateMessageRequestDto {
    UUID senderId;
    UUID receiverId;
    String content;
    ReceiveType receiveType;
    List<BinaryContent> binaryContents;
}
