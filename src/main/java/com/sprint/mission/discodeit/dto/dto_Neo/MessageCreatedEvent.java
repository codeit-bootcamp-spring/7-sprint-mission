package com.sprint.mission.discodeit.dto.dto_Neo;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class MessageCreatedEvent {
//    title: "보낸 사람 (#채널명)"
//    content: "메시지 내용"
//    UUID receiverId;

    UUID channelId;
    String title = "보낸 사람 (#채널명)";
    String content;
}
