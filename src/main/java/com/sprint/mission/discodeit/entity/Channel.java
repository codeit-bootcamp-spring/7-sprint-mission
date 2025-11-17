package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Channel extends BaseEntity {

    private final ChannelType type;    // 채널타입(public, private)
    private String name;
    private String description;

    // 피드백을 통한 수정
    private String SetChannelName(User user, String channelName) {
        if (channelName == null || channelName.isBlank())
            return user.getUsername() + "의 채널";
        else return channelName;

    }

    // PUBLIC
    public Channel(String name, String description, ChannelType type) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // PRIVATE
    public Channel(ChannelType type) {
        super();
        this.name = null;
        this.description = null;
        this.type = type;
    }

    // updateMessage (private는 수정불가)
    public void updateChannelName(String newName) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
        updateTimestamp();
    }

    public void updateChannelDescription(String newDescription) {
        if (newDescription != null) {
            this.description = newDescription;
        }
        updateTimestamp();
    }

}
