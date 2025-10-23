package com.sprint.mission.discodeit.messageroom.domain;

import com.sprint.mission.discodeit.messageroom.domain.vo.MessageVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MessageRoom implements Serializable {
    private static final long serialVersionUID = 3L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;


    private final List<UUID> participants= new ArrayList<>();
    private final List<MessageVo> history = new ArrayList<>();
    private MessageRoomType messageRoomType;
    private String MessageRoomName;

    public static MessageRoom  create(String messageRoomName,MessageRoomType messageRoomType){
        validateMessageRoomName(messageRoomName);
        return new MessageRoom(messageRoomName, messageRoomType);
    }

    public List<UUID> getParticipants() {
        return List.copyOf(participants);
    }

    public List<MessageVo> getHistory() {
        return List.copyOf(history);
    }


    public void addParticipants(UUID id){
        participants.add(id);
    }

    public void addHistory(MessageVo messageVo){
        history.add(messageVo);
    }

    private static void validateMessageRoomName(String messageRoomName){
        if(messageRoomName==null || messageRoomName.length()<1){
            throw new IllegalArgumentException("채팅방 이름을 입력하세요");
        }
    }

    private MessageRoom(String messageRoomName,MessageRoomType messageRoomType) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt=Instant.now();
        this.messageRoomType=messageRoomType;
        this.MessageRoomName=messageRoomName;

    }
}
