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
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;


    private User(String email, String password, String username, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static User create(String email, String password, String username, String phoneNumber) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);
        validatePhoneNumber(phoneNumber);
        return new User(email, password, username, phoneNumber);
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

    public void UpdatedPassword(String password) {
        validatePassword(password);
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }

    public void UpdatedEmail(String email) {
        validateEmail(email);
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateUsername(String username) {
        validateUsername(username);
        this.username = username;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePassword(String password) {
        validatePassword(password);

        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.updatedAt = System.currentTimeMillis();
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


    //+++++++++++++++++++++ 추가 기능 ++++++++++++++++++++++++++++++

    //친구 요청 보내기
    public FriendRequest sendFriendRequestTo(User target) {
        if (this.id.equals(target.getId())) {
            throw new CannotSendFriendRequestToSelfException();
        }
        return new FriendRequest(this.id, target.getId());
    }

    //친구 요청 수락
    public FriendShip acceptFriendRequest(FriendRequest request) {
        if (!request.getReceiverId().equals(this.id)) {
            throw new CannotAcceptOtherUserRequestException();
        }
        return new FriendShip(request.getSenderId(), this.id);
    }
}
