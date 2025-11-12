package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity{
    private String channelName;
    private final ChannelType channelType;
    private final ChannelVisibility visibility;
    private String description;
    private UUID adminId;
    private List<UUID> memberIds;

    public Channel(ChannelType channelType, ChannelVisibility visibility, String channelName, String description, UUID adminId) {
        this.channelType = channelType;
        this.visibility = visibility;
        this.channelName = channelName;
        this.description = description;
        this.adminId = adminId;
        this.memberIds = new ArrayList<>();
        if(adminId != null) this.memberIds.add(adminId);
    }

    public void setChannelName(String channelName) {
        this.setUpdatedAt();
        this.channelName = channelName;
    }

    public void setDescription(String description) {
        this.setUpdatedAt();
        this.description = description;
    }

    public void setAdmin(UUID adminId) {
        this.setUpdatedAt();
        this.adminId = adminId;
    }

    public List<UUID> getMemberIds() {
        return new ArrayList<>(memberIds);
    }

    public void addMember(UUID userId) {
        this.setUpdatedAt();
        this.memberIds.add(userId);
    }

    public void deleteMember(UUID userId){
        this.setUpdatedAt();
        this.memberIds.remove(userId);
    }

    public void update(String name, String description) {
        boolean flag = false;
        if(name != null && !name.isEmpty()){
            this.channelName = name;
            flag = true;
        }
        if(description != null && !description.isEmpty()){
            this.description = description;
            flag = true;
        }
        if(flag){
           this.setUpdatedAt();
        }
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", channelVisibility=" + visibility +
                ", adminId=" + adminId +
                ", memberIds=" + memberIds +
                str +
                '}';
    }
}
