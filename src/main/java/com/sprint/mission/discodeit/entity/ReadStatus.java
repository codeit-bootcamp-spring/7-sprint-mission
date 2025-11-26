package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
@Entity
@Table(name = "read_statuses",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","channel_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;

    @Column(name="last_read_at",nullable = false) @CreationTimestamp
    private Instant readLastTime;

    public static ReadStatus createReadStatusFactory(User user,Channel channel){
        return new ReadStatus(user,channel
                ,Instant.now().minus(Duration.ofDays(1)));
    }
    public static ReadStatus createReadStatusWithDtoFactory(User user,Channel channel,Instant readLastTime){
        return new ReadStatus(user,channel
                ,readLastTime);
    }

}
