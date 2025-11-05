package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class MessageDto extends Entity {
    private final String content;
    private final UserDto sender;
    private final boolean isMarkDown;


    public MessageDto(String content, UserDto sender, boolean isMarkDown) {
        this.content = content;
        this.sender = sender;
        this.isMarkDown = isMarkDown;
    }

    public MessageDto(UUID id, String content, UserDto sender, boolean isMarkDown) {
        super(id);
        this.content = content;
        this.sender = sender;
        this.isMarkDown = isMarkDown;
    }

    public String getContent() {
        return content;
    }

    public UserDto getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "content='" + content + '\'' +
                ", sender=" + sender.getName() +
                ", isMarkDown=" + isMarkDown +
                '}';
    }

    public boolean isMarkDown() {
        return isMarkDown;
    }
}
