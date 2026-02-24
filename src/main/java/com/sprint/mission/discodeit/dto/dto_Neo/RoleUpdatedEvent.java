package com.sprint.mission.discodeit.dto.dto_Neo;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class RoleUpdatedEvent {
//    title: "권한이 변경되었습니다."
//    content: "USER -> CHANNEL_MANAGER"
    UUID receiverId;
    String title = "권한이 변경되었습니다.";
    String content;

    public RoleUpdatedEvent(UUID receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
    }
}
