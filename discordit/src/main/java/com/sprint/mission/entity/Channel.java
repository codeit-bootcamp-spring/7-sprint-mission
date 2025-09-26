package com.sprint.mission.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends BaseEntity {

    private ChannelType type;

    private List<User> members; // 채널 등록 멤버 목록
    private List<User> onlineUsers; // 접속 중 멤버
    private List<User> moderators;// 운영진

    public Channel(ChannelType type, List<User> moderators) {
        this.type = type;
        this.members = new ArrayList<>();
        this.onlineUsers = new ArrayList<>();
        this.moderators = moderators;
    }

    private enum ChannelType {
        TEXT("텍스트"),
        VOICE("음성");

        ChannelType(String description) {
        }
    }

}
