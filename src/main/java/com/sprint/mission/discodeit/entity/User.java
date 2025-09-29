package com.sprint.mission.discodeit.entity;

import java.util.Objects;

public class User extends BaseEntity {
    private String username; // 유저 이름 ( 별명 x)
    private String password;
    private String email;
    private UserState userState; // 0: 오프라인 , 1: 온라인

    public User(String username, String password, String email) {
        this.username = verifyUsername(username);
        this.password = verifyPassword(password);;
        this.email = verifyEmail(email);
        this.userState = UserState.ONLINE;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        if(userState == null) { throw new IllegalArgumentException("userState cannot be null"); }
        if (this.userState != userState) {
            this.userState = userState;
            reUpdatedAt();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        String v = verifyUsername(username);
        if (!Objects.equals(this.username, v)){
            this.username = v;
            reUpdatedAt();
        }
    }

    public void setPassword(String password) {
        String v = verifyPassword(password);
        if(!Objects.equals(this.password, v)){
            this.password = v;
            reUpdatedAt();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String v = verifyEmail(email);
        if(!Objects.equals(this.email, v)){
            this.email = v;
            reUpdatedAt();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void update(String username, String password, String email) {
        if(username != null) setUsername(username);
        if(password != null) setPassword(password);
        if(email != null) setEmail(email);
    }

    private static String verifyUsername(String username) {
        if (username == null || username.isBlank()) {throw new IllegalArgumentException("username cannot be null");}
        String s = username.trim();
        if (s.length() > 20 || s.length() < 2) { throw new IllegalArgumentException("username length must be between 2 and 20 characters");}
        return s;
    }

    private static String verifyPassword(String password) {
        if(password == null || password.isBlank()) {throw new IllegalArgumentException("password cannot be null");}
        if(password.length() > 50 || password.length() < 7) {throw new IllegalArgumentException("password length must be between 50 and 7");}
        return password;
    }

    private static String verifyEmail(String email) {
        if (email == null || email.isBlank()) {throw new IllegalArgumentException("email cannot be null");}
        String s = email.trim().toLowerCase();
        if(!s.contains("@")) { throw new IllegalArgumentException("email must contain '@'");}
        return s;
    }
}
