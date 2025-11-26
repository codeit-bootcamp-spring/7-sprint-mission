package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.exception.ErrorType;
import com.sprint.mission.discodeit.entity.exception.ValidationException;
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


    private void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException(ErrorType.INVALID_EMAIL);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 4) {
            throw new ValidationException(ErrorType.INVALID_PASSWORD);
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException(ErrorType.INVALID_USERNAME);
        }
    }

}
