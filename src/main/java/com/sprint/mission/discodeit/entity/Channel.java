package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private String name;                    //채널명
    private String description;             //채널 설명
    private final UUID managerId;           //채널 생성자 UUID
    private List<UUID> userIds;               //채널 참가자
    private ChannelType publicType;         //공개, 비공개 여부

    //Constructor
    private Channel(UUID managerId, String name, String description, List<UUID> userIds, ChannelType publicType) {
        this.managerId = managerId;
        this.name = name;
        this.description = description;
        this.userIds = userIds;
        this.publicType = publicType;
    }

    public static Channel createPublic(UUID managerId, String name, String description) {
        return new Channel(
                managerId,
                name,
                description,
                null,
                ChannelType.PUBLIC
        );
    }

    public static Channel createPrivate(UUID managerId, List<UUID> userIds) {
        String privateName = "비밀방" + UUID.randomUUID().toString().replace("-", "");
        return new Channel(
                managerId,
                privateName,
                null,
                userIds,
                ChannelType.PRIVATE
        );
    }

    //update name
    public Channel update(String name, String description){
        super.update();
        this.name = name;
        this.description = description;
        return this;
    }
}
