package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.util.UUID;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
@ToString
public class Notifications extends BaseEntity {
    @Column(name = "receiver_id", nullable = false)
    UUID receiverId;

    @Column(name = "title", nullable = false, length = 255)
    String title;

    @Column(name = "content", nullable = false, length = 1000)
    String content;
}
