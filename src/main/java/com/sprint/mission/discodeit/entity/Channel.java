package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity{
    private String channelName;
    private final ChannelType channelType;
    private UUID adminId;
    private List<UUID> memberIds;

    public Channel(ChannelType channelType, String channelName, UUID adminId) {
        this.channelType = channelType;
        this.channelName = channelName;
        this.adminId = adminId;
        this.memberIds = new ArrayList<>();
        this.memberIds.add(adminId);
    }

    public void setChannelName(String channelName) {
        this.setUpdatedAt();
        this.channelName = channelName;
    }

    public void setAdmin(UUID adminId) {
        this.setUpdatedAt();
        this.adminId = adminId;
    }

    public List<UUID> getMembers() {
        return new ArrayList<>(memberIds);
    }

    public void addMember(UUID userId) {
        this.setUpdatedAt();
        this.memberIds.add(userId);
    }

    public void delMember(UUID userId){
        this.setUpdatedAt();
        this.memberIds.remove(userId);
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", adminId=" + adminId +
                ", memberIds=" + memberIds +
                str +
                '}';
    }
}
