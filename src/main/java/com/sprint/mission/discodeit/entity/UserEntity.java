package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.domain.UserStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity extends BaseUpdatableEntity{
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, name = "last_active_at")
    private Instant lastActiveAt;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name ="profile_id")
    private BinaryContentEntity profileId;
}
