package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.vo.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MessageRoom {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;


    private final List<UUID> participants= new ArrayList<>();
    private final List<Message> history = new ArrayList<>();
    private MessageRoomType messageRoomType;
    private String MessageRoomName;


    public MessageRoom() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public List<UUID> getParticipants() {
        return List.copyOf(participants);
    }

    public List<Message> getHistory() {
        return List.copyOf(history);
    }

    public void addParticipants(UUID id){
        participants.add(id);
    }

    public void addHistory(Message message){
        history.add(message);
    }
}
