package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.domain.exception.ErrorType;
import com.sprint.mission.discodeit.domain.exception.ValidationException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity extends BaseUpdatableEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column( name = "last_active_at")
    private Instant lastActiveAt;

    @Column(name = "profile_id")
    private UUID profileId;




}
