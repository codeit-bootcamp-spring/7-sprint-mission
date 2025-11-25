package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.Instant;

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
    private Instant lastActiveAt;

    public boolean isUserOnline(){
        Duration duration = Duration.between(this.lastActiveAt, Instant.now());
        return duration.getSeconds()<300;
    }
}
