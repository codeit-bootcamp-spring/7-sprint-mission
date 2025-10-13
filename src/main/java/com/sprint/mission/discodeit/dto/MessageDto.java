package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;

public class MessageDto extends Entity {
    private final String content;
    private final User sender;
    private final boolean isMarkDown;


    public MessageDto(String content, User sender, boolean isMarkDown) {
        this.content = content;
        this.sender = sender;
        this.isMarkDown = isMarkDown;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public boolean isMarkDown() {
        return isMarkDown;
    }
}
