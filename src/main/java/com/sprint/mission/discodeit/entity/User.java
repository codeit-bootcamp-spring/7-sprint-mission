package com.sprint.mission.discodeit.entity;

public class User extends Common {

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
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", userNickname='" + userNickname + '\'' +
                '}';
    }
}
