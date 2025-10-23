package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;
import com.sprint.mission.discodeit.friendship.domain.FriendShip;
import com.sprint.mission.discodeit.user.domain.exception.CannotAcceptOtherUserRequestException;
import com.sprint.mission.discodeit.user.domain.exception.CannotSendFriendRequestToSelfException;
import com.sprint.mission.discodeit.user.domain.exception.ErrorType;
import com.sprint.mission.discodeit.user.domain.exception.ValidationException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;


    public static User create(String email, String password, String username, String phoneNumber) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);
        validatePhoneNumber(phoneNumber);
        return new User(email, password, username, phoneNumber);
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

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.updatedAt = Instant.now();
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(nickname, user.nickname) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, username, password, email, nickname, phoneNumber);
    }


    //친구 요청 보내기
    public FriendRequest sendFriendRequestTo(User target) {
        return FriendRequest.create(this.id, target.getId());
    }

    //친구 요청 수락
    public FriendShip acceptFriendRequest(FriendRequest request) {
        return FriendShip.create(request.getSenderId(), this.id);
    }

    private User(String email, String password, String username, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.createdAt =Instant.now();
        this.updatedAt = Instant.now();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    private static void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException(ErrorType.INVALID_EMAIL);
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 4) {
            throw new ValidationException(ErrorType.INVALID_PASSWORD);
        }
    }

    private static void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException(ErrorType.INVALID_USERNAME);
        }
    }

    private static void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^\\d{2,3}-?\\d{3,4}-?\\d{4}$")) {
            throw new ValidationException(ErrorType.INVALID_PHONE_NUMBER);
        }
    }
}
