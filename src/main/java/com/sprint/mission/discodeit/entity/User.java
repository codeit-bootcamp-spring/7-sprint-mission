package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.exception.ErrorType;
import com.sprint.mission.discodeit.entity.exception.ValidationException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseUpdatableEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column( name = "last_active_at")
    private Instant lastActiveAt;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;



    public User(String email, String password, String username) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);
        this.username = username;
        this.password = password;
        this.email = email;

    }


    public void updateUsername(String username) {
        validateUsername(username);
        this.username = username;

    }

    public void updatePassword(String password) {
        validatePassword(password);

        this.password = password;

    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;

    }

    public void updateLastActiveAt(Instant lastActiveAt){
        this.lastActiveAt=lastActiveAt;
    }

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
