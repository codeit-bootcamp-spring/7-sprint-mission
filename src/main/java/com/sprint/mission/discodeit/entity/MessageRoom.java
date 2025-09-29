package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MessageRoom {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;


    private final List<User> participants= new ArrayList<>();
    private final List<Message> history = new ArrayList<>();
    private MessageRoomType messageRoomType;
    private String MessageName;



    public void update(){
        this.updatedAt=System.currentTimeMillis();
    }


    public MessageRoom() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public void setMessageRoomType(MessageRoomType messageRoomType) {
        this.messageRoomType = messageRoomType;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public List<Message> getHistory() {
        return history;
    }

    public MessageRoomType getMessageRoomType() {
        return messageRoomType;
    }

    public String getMessageName() {
        return MessageName;
    }

    public void setMessageRoomName(String messageRoomName) {
        if(this.messageRoomType==MessageRoomType.DM){
            System.out.println("서버 내의 채팅방에서만 이름을 설정할 수 있습니다.");
        }
        update();
        MessageName = messageRoomName;
    }
}
