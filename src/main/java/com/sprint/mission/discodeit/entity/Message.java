package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.Arrays;

public class Message extends BaseEntity {
    private final String sender;
    private final String receiver;
    private String message;
    private final Channel ch;

    public Message(String sender, String receiver, String message) {
        super();
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.ch = null;
    }

    public Message(String sender, Channel ch, String message) {
        super();
        this.sender = sender;
        this.ch = ch;
        this.receiver = null;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
    public String getMessage() {
        return message;
    }

    public void updateMessage(String message) {
        this.message = message;
        updateTimestamp();
    }
}

// User 쪽에 void chat(User receiver, String c){ Message m =  new Message(this, receiver, c, ...) }를 만들어서
// Message클래스에서는 로그를 관리하는게 더 편할까?? 이러면 객체가 계속 만들어지나? receiver가 있으니 만들어진걸 계속 쓸 수 있나?
// 그러면 내가 보낸 채팅 뿐만 아니라 저쪽이 보낸 것도 한번에 합쳐서 전체 로그를 만들 수 있을지도???
// 개어렵네
// TestMessage 클래스로 ->



