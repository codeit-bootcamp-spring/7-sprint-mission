package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel{

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType type;



    public Channel(String name, boolean isPrivate) {
        validateChannelName(name);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt=Instant.now();
        this.name = name;
        if(isPrivate){
            this.type=ChannelType.PRIVATE;
        }else{
            this.type=ChannelType.PUBLIC;
        }
//        if(members==null){
//            this.members=new ArrayList<>();
//        } else {
//            this.members=members;
//        }
    }

    public void updateChannelName(String name){
        validateChannelName(name);
        this.name =name;
    }

    private void validateChannelName(String name){
        if(name==null || name.length()<1){
            throw new IllegalArgumentException("채널 이름을 입력하세요");
        }
    }

//    public List<UUID> getHistory() {
//        return List.copyOf(history);
//    }
//
//
//    public void sendMessage(UUID messageId){
//        history.add(messageId);
//    }
//
//    public void deleteMessage(UUID messageId){
//        history.remove(messageId);
//    }
//
//    public void addChannelMember(UUID userId){
//        if (members
//                .stream()
//                .anyMatch(uuid -> uuid.equals(userId))){
//            throw new IllegalArgumentException("해당 유저는 이미 채널에 있습니다.");
//        }
//        members.add(userId);
//
//    }
}