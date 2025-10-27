package com.sprint.mission.discodeit.domain.channel;

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

    private final UUID serverId;
    private String channelName;

    private List<ChannelMember> members = new ArrayList<>();

    private final List<Message> history = new ArrayList<>();

    public Channel(String channelName,UUID serverId) {
        validateChannelName(channelName);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt=Instant.now();
        this.serverId=serverId;
        this.channelName=channelName;
    }

    public void updateChannelName(String name){
        validateChannelName(name);
        this.channelName=channelName;
    }

    private void validateChannelName(String name){
        if(name==null || name.length()<1){
            throw new IllegalArgumentException("채널 이름을 입력하세요");
        }
    }

    public List<Message> getHistory() {
        return List.copyOf(history);
    }



}
