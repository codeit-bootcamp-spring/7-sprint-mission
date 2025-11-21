package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.*;

@Getter
@ToString
@Setter
@Entity
@Builder
@Table(name = "user_statuses")
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "last_active_at",nullable = false)
    private Instant lastOnlineTime;

    public boolean isUserOnline(){
        Duration duration = Duration.between(this.lastOnlineTime, now());
        return duration.getSeconds()<300;
    }
}
