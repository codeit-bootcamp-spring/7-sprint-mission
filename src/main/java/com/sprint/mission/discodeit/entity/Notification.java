package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@Table(name = "notifications")
@ToString
public class Notification extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    protected Notification() {}

    public Notification(User receiver, String title, String content) {
        this.receiver = Objects.requireNonNull(receiver);
        this.title = Objects.requireNonNull(title);
        this.content = Objects.requireNonNull(content);

        if (this.title.isBlank()) {
            throw new IllegalArgumentException("title cannot be blank");
        }

        if (this.content.isBlank()) {
            throw new IllegalArgumentException("content cannot be blank");
        }
    }
}
