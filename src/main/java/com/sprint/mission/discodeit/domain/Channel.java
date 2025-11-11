package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 3L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private final List<UUID> members;
    private final boolean isPrivate;
    private final List<UUID> history=new ArrayList<>();


    public Channel(String channelName, List<UUID> membersId, boolean isPrivate) {
        validateChannelName(channelName);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt=Instant.now();
        this.channelName=channelName;
        this.isPrivate=isPrivate;
        this.members=membersId;
    }

    public void updateChannelName(String name){
        validateChannelName(name);
        this.channelName=name;
    }

    private void validateChannelName(String name){
        if(name==null || name.length()<1){
            throw new IllegalArgumentException("채널 이름을 입력하세요");
        }
    }

    public List<UUID> getHistory() {
        return List.copyOf(history);
    }

    public List<UUID> getChannelMember(){
        return List.copyOf(members);
    }


    public void sendMessage(UUID messageId){
        history.add(messageId);
    }

    public void deleteMessage(UUID messageId){
        history.remove(messageId);
    }

    public void addChannelMember(UUID userId){
        if (members
                .stream()
                .anyMatch(uuid -> uuid.equals(userId))){
            throw new IllegalArgumentException("해당 유저는 이미 채널에 있습니다.");
        }
        members.add(userId);

    }
}
