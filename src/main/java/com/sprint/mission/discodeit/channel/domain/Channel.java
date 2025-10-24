package com.sprint.mission.discodeit.channel.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Channel implements Serializable {
    private static final long serialVersionUID = 3L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;


    private final List<UUID> participants= new ArrayList<>();
    private final List<UUID> history = new ArrayList<>();

    private String MessageRoomName;

    public static Channel createChannel(String messageRoomName){
        validateChannelName(messageRoomName);
        return new Channel(messageRoomName);
    }

    public static Channel createDM(String messageRoomName){
        validateChannelName(messageRoomName);
        return new Channel(messageRoomName);
    }

    private Channel(String messageRoomName) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt=Instant.now();

        this.MessageRoomName=messageRoomName;

    }

    private static void validateChannelName(String name){
        if(name==null || name.length()<1){
            throw new IllegalArgumentException("채널 이름을 입력하세요");
        }
    }



    public List<UUID> getParticipants() {
        return List.copyOf(participants);
    }

    public List<UUID> getHistory() {
        return List.copyOf(history);
    }


    public void addParticipants(UUID id){
        participants.add(id);
    }

    public void addHistory(UUID id){
        history.add(id);
    }


}
