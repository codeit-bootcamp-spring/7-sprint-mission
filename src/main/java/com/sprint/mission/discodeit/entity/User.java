package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.Common;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private String UserEmail;
    private String password;
    private  String userName;
    private  String userNickname;





    public User(String userId,String password,String userName,String userNickname)
    {
           this.UserEmail = userId;
           this.userName = userName;
           this.password = password;
           this.userNickname = userNickname;
    }



  /*  public String getUserNickname() {
        return userNickname;
    }*/

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

  /*  public String getUserName() {
        return userName;
    }*/

    public void setUserName(String userName) {
        this.userName = userName;
    }

 /*   public String getUserId() {
        return UserEmail;
    }*/

    public void setUserId(String userId) {
        this.UserEmail = userId;
    }

    /*public String getPassword() {
        return password;
    }*/

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "아이디 = '" + UserEmail + '\'' +
                ", 비번 = '" + password + '\'' +
                ", 이름 = '" + userName + '\'' +
                ", 닉네임 = '" + userNickname + '\'' +
                ", 닉네임 = '" + this.getId() + '\'' +
                '}';
    }

    //업데이트하면 하난하나 적용해서 트루로체크해
    //끝가지가면 트루 아니면 false된다
    public void update(String newUsername, String newEmail, String newPassword, String newUserNickname) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.userName)) {
            this.userName = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.UserEmail)) {
            this.UserEmail = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            anyValueUpdated = true;
        }
        if (newUserNickname != null && !newUserNickname.equals(this.userNickname)) {
            this.userNickname = newUserNickname;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.setUpdatedAt(Instant.now().getEpochSecond());
        }
    }





}
