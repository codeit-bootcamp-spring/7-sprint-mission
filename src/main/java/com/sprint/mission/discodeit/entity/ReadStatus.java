package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "read_statuses")
@AttributeOverride(name = "id", column = @Column(name = "read_status_id"))
public class ReadStatus extends BaseUpdatableEntity {
    //user_id, channel_id 복합 유니크 제약 조건 걺

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "last_read_at")
    private Instant lastReadAt;

}

