package com2.sprint.mission.discodeit.entity;

/**
 * 디스코드 채널을 나타내는 엔티티
 */
public class Channel extends BaseEntity {
    private String name;        // 채널 이름
    private String description; // 채널 설명

    public Channel(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    // Getter
    public String getName() { return name; }
    public String getDescription() { return description; }

    // 정보 수정
    public void update(String name, String description) {
        this.name = name;
        this.description = description;
        touch();
    }
}