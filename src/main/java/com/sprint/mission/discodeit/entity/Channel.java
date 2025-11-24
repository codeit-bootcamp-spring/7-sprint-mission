package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Channel extends BaseUpdatableEntity{
    private String channelName;
    private final ChannelType channelType;
    private final ChannelVisibility visibility;
    private String description;
    private UUID adminId; // 요구사항 API에 필요없는 필드로 제외하였습니다. 이후 사용해도 되면 관련해서 추가할 예정 입니다.
    private List<UUID> memberIds = new ArrayList<>();

    public Channel() {
        this.channelType = ChannelType.MESSAGE;
        this.visibility = ChannelVisibility.PRIVATE;
    }

    public Channel(String channelName, String description) {
        this.channelType = ChannelType.MESSAGE;
        this.visibility = ChannelVisibility.PUBLIC;
        this.channelName = channelName;
        this.description = description;
    }

    public List<UUID> getMemberIds() {
        return new ArrayList<>(memberIds);
    }

    public void addMember(UUID userId) {
        this.memberIds.add(userId);
    }

    public void deleteMember(UUID userId){
        this.memberIds.remove(userId);
    }

    public void update(String name, String description) {
        if(name != null && !name.isEmpty()){
            this.channelName = name;
        }
        if(description != null && !description.isEmpty()){
            this.description = description;
        }
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", channelVisibility=" + visibility +
                ", description=" + description +
                ", memberIds=" + memberIds +
                str +
                '}';
    }
}
