package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseEntity {

    @Column(nullable = false, length = 1000)
    private String content;

    // User (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    // Channel (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    // BinaryContent (1:1)
    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private BinaryContent binaryContent;

    @Builder
    public Message(String content, User user, Channel channel) {
        this.content = content;
        this.user = user;
        this.channel = channel;
    }

    public void update(String content) {
        this.content = content;
    }

    public void updateBinary(BinaryContent binaryContent) {
        this.binaryContent = binaryContent;
    }

}
