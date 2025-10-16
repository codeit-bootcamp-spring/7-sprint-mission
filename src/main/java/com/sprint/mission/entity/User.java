package com.sprint.mission.entity;

import java.io.Serializable;

public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String password;
    private  String userName;
    private  String userNickname;





    public User(String userId,String password,String userName,String userNickname)
    {
           this.userId = userId;
           this.userName = userName;
           this.password = password;
           this.userNickname = userNickname;
    }



    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "아이디 = '" + userId + '\'' +
                ", 비번 = '" + password + '\'' +
                ", 이름 = '" + userName + '\'' +
                ", 닉네임 = '" + userNickname + '\'' +
                ", 닉네임 = '" + this.getId() + '\'' +
                '}';
    }
}
