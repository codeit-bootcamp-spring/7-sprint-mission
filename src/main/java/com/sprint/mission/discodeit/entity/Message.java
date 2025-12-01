package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor
public class Message extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 2000)
    private String content;

    // Message → Channel (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    // Message → User (N:1, 작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // ✅ 여기 추가!
    public UUID getChannelId() {
        return channel != null ? channel.getId() : null;
    }
}

