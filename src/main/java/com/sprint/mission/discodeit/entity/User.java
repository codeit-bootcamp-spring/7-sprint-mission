package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private List<FriendRequest> receivedFriendRequests=new ArrayList<>();
    private List<MessageRoom> MyMessageRooms = new ArrayList<>();
    private List<Channel> MyChannels = new ArrayList<>();
    private List<User> friends = new ArrayList<>();
//    private Language language;



    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }


    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            System.out.println("비밀번호를 8글자 이상 적어주세요.");
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


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<MessageRoom> getMyMessageRooms() {
        return MyMessageRooms;
    }

    public List<Channel> getMyChannels() {
        return MyChannels;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<FriendRequest> getReceivedFriendRequests() {
        return receivedFriendRequests;
    }

    public void updateUsername(String newUsername){
        this.updatedAt=System.currentTimeMillis();
        this.username=newUsername;
    }
    public void updatePassword(String newPassword){
        if (newPassword == null || newPassword.length() < 8) {
            System.out.println("비밀번호를 8글자 이상 적어주세요.");
            return;
        }
        this.updatedAt=System.currentTimeMillis();
        this.password=newPassword;
    }
    public void updateEmail(String newEmail){
        if (email == null || email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
