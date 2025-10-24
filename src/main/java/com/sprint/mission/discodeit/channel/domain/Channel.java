package com.sprint.mission.discodeit.channel.domain;

import com.sprint.mission.discodeit.server.domain.Server;
import lombok.Getter;
import lombok.Setter;

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
    //message의 id
    private final List<UUID> history = new ArrayList<>();

    public static Channel create(String ChannelName, UUID serverId){
        validateChannelName(ChannelName);
        return new Channel(ChannelName,serverId);
    }

    private Channel(String channelName,UUID serverId) {
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

    private static void validateChannelName(String name){
        if(name==null || name.length()<1){
            throw new IllegalArgumentException("채널 이름을 입력하세요");
        }
    }

    public List<UUID> getHistory() {
        return List.copyOf(history);
    }

    public void addHistory(UUID id){
        history.add(id);
    }


}
