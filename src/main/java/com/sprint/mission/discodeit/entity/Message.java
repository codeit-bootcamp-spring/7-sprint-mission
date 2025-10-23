package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.UUID;

@Getter @Setter //Setter의 경우 추후 수정 가능성
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Message extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    private String content;
    private final UUID channelId;
    private final UUID userId;

    public Message(String content, UUID channelId, UUID userId) {
        this.content = content;
        this.channelId = channelId;
        this.userId = userId;
    }

}
