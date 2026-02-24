package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "notifications")
@Getter
public class Notification extends BasicEntity {

    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    private Notification(UUID receiverId, String title, String content) {
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
    }

    public static Notification create(UUID receiverId, String title, String content) {
        return new Notification(receiverId, title, content);
    }

}
