package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Builder
public class Channel extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private String name;                    //채널명
    private String description;             //채널 설명
    private final UUID managerId;           //채널 생성자 UUID
    private List<UUID> users;               //채널 참가자
    private ChannelType publicType;         //공개, 비공개 여부

    //Constructor
    public Channel(UUID managerId, String name, String description) {
        this.managerId = managerId;
        this.name = name;
        this.description = description;
        this.publicType = ChannelType.PUBLIC;
    }

    public Channel(UUID managerId, List<UUID> users) {
        this.managerId = managerId;
        this.users = users;
        this.publicType = ChannelType.PRIVATE;
    }

    //update name
    public Channel update(String name, String description){
        super.update();
        this.name = name;
        this.description = description;
        return this;
    }
}
