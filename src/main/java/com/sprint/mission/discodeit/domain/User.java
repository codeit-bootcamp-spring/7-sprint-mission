package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.exception.ErrorType;
import com.sprint.mission.discodeit.domain.exception.ValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@Setter
public class User {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String username;
    private String password;
    private String email;

    private String profileId;
    private Instant lastActiveAt;


    public User(String email, String password, String username) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);


        this.username = username;
        this.password = password;
        this.email = email;
        this.lastActiveAt=Instant.now();
        this.updatedAt=Instant.now();
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
