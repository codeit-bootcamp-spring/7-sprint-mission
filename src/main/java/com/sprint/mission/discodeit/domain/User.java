package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.exception.ErrorType;
import com.sprint.mission.discodeit.domain.exception.ValidationException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    private String phoneNumber;
    private UserStatus userStatus;

    private final List<UUID> receivedInvitations = new ArrayList<>();
    private final List<UUID> friends = new ArrayList<>();
    private final List<UUID> servers =new ArrayList<>();
    private UUID profile;


    public User(String email, String password, String username, String phoneNumber) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);
        validatePhoneNumber(phoneNumber);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userStatus = new UserStatus(OnlineStatus.OFFLINE);
        this.profile = null;
    }

    public void setProfile(UUID profile){
        this.profile =profile;
    }

    public void sendFriendInvitation(User user){
        if (receivedInvitations.contains(user.id)){
            acceptInvitation(user);
            receivedInvitations.removeAll(List.of(user.id));
        }
        //같은 클래스의 객체끼리는 이런식으로 가능?
        user.receivedInvitations.add(this.id);
    }

    public void acceptInvitation(User user){
        this.friends.add(user.id);
        user.friends.add(this.id);
    }

    public boolean checkOnline(){
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


    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.updatedAt = Instant.now();
    }

    public void markOnline(){
        if(this.checkOnline()){
            return;
        }
        this.userStatus=new UserStatus(OnlineStatus.ONLINE);
    }

    public void markOffline(){
        this.userStatus =  new UserStatus(OnlineStatus.OFFLINE);
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

    private void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^\\d{2,3}-?\\d{3,4}-?\\d{4}$")) {
            throw new ValidationException(ErrorType.INVALID_PHONE_NUMBER);
        }
    }

}
