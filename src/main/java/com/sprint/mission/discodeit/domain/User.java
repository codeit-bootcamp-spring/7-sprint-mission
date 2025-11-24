package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.exception.ErrorType;
import com.sprint.mission.discodeit.domain.exception.ValidationException;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class User {

    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String username;
    private String password;
    private String email;
    private UserStatus userStatus;
    private UUID profileId;

    private Instant lastAt;


    public User(String email, String password, String username) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);

        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.userStatus = new UserStatus(OnlineStatus.OFFLINE);

    }

    public void setProfile(UUID profileId) {
        this.profileId = profileId;
    }


    public boolean checkOnline() {
        return userStatus.isOnline();
    }

    public void UpdatedPassword(String password) {
        validatePassword(password);
        this.password = password;

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


    public void markOnline(Instant lastAt) {
        this.userStatus = new UserStatus(OnlineStatus.ONLINE, lastAt);
    }

    public void markOffline(Instant lastAt) {
        this.userStatus = new UserStatus(OnlineStatus.OFFLINE, lastAt);
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
