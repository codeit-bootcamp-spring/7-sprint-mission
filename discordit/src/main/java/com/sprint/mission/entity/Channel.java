package com.sprint.mission.entity;

import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID uuid = UUID.randomUUID();
    private Long createdAt;
    private Long updatedAt;
    private ChannelType type;

    private List<User> members; // 채널 등록 멤버 목록
    private List<User> onlineUsers; // 접속 중 멤버
    private List<User> moderators;// 운영진

    private enum ChannelType {
        TEXT("텍스트"),
        VOICE("음성");

        ChannelType(String description) {
        }
    }

}
