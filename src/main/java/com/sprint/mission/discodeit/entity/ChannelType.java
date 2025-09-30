package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    CHAT("대화"),
    VIDEO("영상"),
    DIRECT("개인"),
    TEST("테스트");

    // 3. 필드 선언 (private final로 캡슐화)
    private final String desc;

    // 4. 생성자 선언 (상수가 생성될 때 호출됨)
    ChannelType(String desc) {
        this.desc = desc;
    }
    public String getDescription() {
        return desc;
    }
}
