package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Channel extends BaseEntity {

    public String channelName;
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

    public Channel(User user, ChannelType type) {
        super();
        this.channelAdmin = user;
        this.channelName = user.getUserName() + "의 채널";
        this.type = type;
        this.members = new ArrayList<>();
        members.add(user);
    }

    public Channel(User user, String channelName, ChannelType type) {
        this(user, type);
        this.channelName = channelName;
    }

    // getter
    public ChannelType getType() {
        return type;
    }

    public User getChannelAdmin() {
        return channelAdmin;
    }

    public List<User> getMembers() {
        return members;
    }

    public String getChannelName() {
        return channelName;
    }

    // update
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        updateTimestamp();
    }

    public void updateMembers(User user) {
        if (user != channelAdmin) {
            if (members.contains(user)) {
                members.remove(user);  // 이미 있으면 제거
            } else {
                members.add(user);     // 없으면 추가
            }
            updateTimestamp();
        }
        else System.out.println("맴버 삭제 불가");
    }

    @Override
    public String toString() {

        String membersName = members.stream().map(User::getUserName)
                .toList().toString();

        return channelName + '{' +
                " 관리자: " + channelAdmin.getUserName() +
                ", 채널유형: " + type +
                ", 생성일자: " + createdAt +
                ", 맴버: " + membersName +
                " }";
    }
}
