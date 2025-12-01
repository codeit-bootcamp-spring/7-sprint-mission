package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor
public class Channel extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    // 공개채널: 설명, 비공개채널: null
    @Column(length = 200)
    private String description;

    // 비공개채널의 경우 생성자/메서드에서 참여자 관리 (별도 테이블 존재 가능)
    // ...필요시 양방향 매핑 구성

    public Channel(String name, ChannelType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
