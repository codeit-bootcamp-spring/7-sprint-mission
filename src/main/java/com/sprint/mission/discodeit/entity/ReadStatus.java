package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "read_statuses")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false, unique = true)
    private Channel channel;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;

//    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
//        this.user = user;
//        this.channel = channel;
//        this.lastReadAt = lastReadAt;
//    }
}
