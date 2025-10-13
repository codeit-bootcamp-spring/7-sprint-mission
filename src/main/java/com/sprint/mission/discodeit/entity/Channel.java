package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity {

    private String channelName;
    private final ChannelType type;    // 채널타입
    private final User channelAdmin;
    private final List<User> members;

    public enum ChannelType {
        TEXT("텍스트"), VOICE("음성"),
        FORUM("포럼");

        private final String descType;

        ChannelType(String descType) {
            this.descType = descType;
        }

        public String getDescType() {
            return descType;
        }

    }
    public Channel(User user, String channelName, ChannelType type) {
        super();
        this.channelAdmin = user;
        if (channelName == null || channelName.isBlank()) {
            this.channelName = user.getUserName() + "의 채널";
        }
        else this.channelName = channelName;

        this.type = type;
        this.members = new ArrayList<>();
        members.add(user);
    }

    public Channel(User user, ChannelType type) {
        this(user, null, type);
    }

    // getter
    public ChannelType getType() {return type;}
    public User getChannelAdmin() {return channelAdmin;}
    public List<User> getMembers() {return members;}
    public String getChannelName() {return channelName;}

    // updateMessage
    public void changeChannelName(String channelName) {
        if (channelName == null || channelName.isEmpty()) {
            throw new InvalidInputException("잘못된 입력");
        }
        this.channelName = channelName;
        updateTimestamp();
    }
    public boolean addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
            updateTimestamp();
            return true;
        }
        return false;
    }
    public boolean removeMember(User user) {
        if(members.contains(user)) {
            members.remove(user);
            updateTimestamp();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", type=" + type +
                ", channelAdmin=" + channelAdmin +
                ", members=" + members +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
