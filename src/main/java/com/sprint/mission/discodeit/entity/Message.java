package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{
    private UUID receiverId;
    private UUID senderId;
    private String message;

    protected Message() {
        super();
    }

    public static Message create(UUID receiverId, UUID senderId, String message) {
        if(receiverId == null){
            throw new IllegalArgumentException("수신 받을 사용자 정보가없습니다.");
        }
        if(senderId == null){
            throw new IllegalArgumentException("수신한 사용자 정보가없습니다.");
        }
        if(message == null || message.isBlank()){
            throw new IllegalArgumentException("메새지를 입력해주세요.");
        }
        Message msg = new Message();
        msg.receiverId = receiverId;
        msg.senderId = senderId;
        msg.message = message;
        return msg;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }
}
