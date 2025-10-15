package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private final List<UUID> myMessageRooms = new ArrayList<>();
    private final List<UUID> myChannels = new ArrayList<>();
    private final List<UUID> friends = new ArrayList<>();
    private final List<UUID> myFriendRequest = new ArrayList<>();





    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public User(String username, String password, String email, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) {
            System.out.println("비밀번호를 4글자 이상 적어주세요.");
        } else {
            this.password = password;
        }
    }

    public void setEmail(String email) {
        if (email == null || email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')) {
            System.out.println("올바른 이메일 형식으로 적어주세요");
        } else {
            this.email = email;
        }

    }

    public void updateUsername(String newUsername){
        this.updatedAt=System.currentTimeMillis();
        this.username=newUsername;
    }
    public void updatePassword(String newPassword){
        if (newPassword == null || newPassword.length() < 4) {
            System.out.println("비밀번호를 4글자 이상 적어주세요.");
            return;
        }
        this.updatedAt=System.currentTimeMillis();
        this.password=newPassword;
    }
    public void updateEmail(String newEmail){
        if ( email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')) {
            System.out.println("올바른 이메일 형식으로 적어주세요");
        }
        this.updatedAt=System.currentTimeMillis();
        this.email=newEmail;
    }
    public void updateNickname(String newNickname){
        this.updatedAt=System.currentTimeMillis();
        this.nickname=newNickname;
    }
    public void updatePhoneNumber(String newPhoneNumber){
        this.updatedAt=System.currentTimeMillis();
        this.phoneNumber=newPhoneNumber;
    }


    public List<UUID> getMyMessageRooms() {
        return List.copyOf(myMessageRooms);
    }

    public List<UUID> getMyChannels() {
        return List.copyOf(myChannels);
    }

    public void addMyChannel(UUID uuid){
        myChannels.add(uuid);
    }


    public List<UUID> getFriends() {
        return List.copyOf(friends);
    }

    public void addFriend(UUID id){
        friends.add(id);
    }

    public List<UUID> getMyFriendRequest() {
        return List.copyOf(myFriendRequest);
    }
    public void addFriendRequest(UUID id){
        myFriendRequest.add(id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(nickname, user.nickname) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(myMessageRooms, user.myMessageRooms) && Objects.equals(myChannels, user.myChannels) && Objects.equals(friends, user.friends) && Objects.equals(myFriendRequest, user.myFriendRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, username, password, email, nickname, phoneNumber, myMessageRooms, myChannels, friends, myFriendRequest);
    }
}
