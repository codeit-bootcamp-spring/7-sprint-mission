package com.sprint.mission.discodeit.data;

public enum DataKey {
    USER("users.txt", "USER"),
    CHANNEL("channels.txt", "CHANNEL"),
    PARTICIPATION("participation.txt", "PARTICIPATION"),
    DIRECT_MESSAGE("directmessages.txt", "DIRECT_MESSAGE"),
    CHANNEL_MESSAGE("channelmessages.txt", "CHANNEL_MESSAGE");

    // 3. 필드 선언 (private final로 캡슐화)
    private final String desc;
    private final String name;

    // 4. 생성자 선언 (상수가 생성될 때 호출됨)
    DataKey(String desc, String name) {
        this.desc = desc;
        this.name = name;
    }
    public String getDescription() {
        return desc;
    }

    public String getName() {
        return name;
    }
}

