package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BasicEntity{

    private final UUID sender; //보내는 사람
    private final UUID receiver; // 받는 사람
    private String content; // 메시지 내용

    public Message(UUID sender, UUID receiver, String content) {
        super();
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) { //메시지 보내기
        this.content = content;
        update();
    }
}
