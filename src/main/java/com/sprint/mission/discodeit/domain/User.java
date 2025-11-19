package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.exception.ErrorType;
import com.sprint.mission.discodeit.domain.exception.ValidationException;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String username;
    private String password;
    private String email;
    private UserStatus userStatus;

    private final List<UUID> receivedInvitations = new ArrayList<>();
    private final List<UUID> friends = new ArrayList<>();
    private UUID profile;


    public User(String email, String password, String username) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.username = username;
        this.password = password;
        this.email = email;
        this.userStatus = new UserStatus(OnlineStatus.OFFLINE);

    }

    public void setProfile(UUID profile) {
        this.profile = profile;
    }


    public boolean checkOnline() {
        return userStatus.isOnline();
    }

    public void UpdatedPassword(String password) {
        validatePassword(password);
        this.password = password;
        this.updatedAt = Instant.now();
    }

    public void updateUsername(String username) {
        validateUsername(username);
        this.username = username;
        this.updatedAt = Instant.now();
    }

    public void updatePassword(String password) {
        validatePassword(password);

        this.password = password;
        this.updatedAt = Instant.now();
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;
        this.updatedAt = Instant.now();
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
