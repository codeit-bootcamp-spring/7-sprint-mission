package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.vaildator.UserVaildator;

public class User extends BaseEntity{
    private String userName;
    private String nickName;
    private String email;
    private String phoneNum;
    private final String userId;
    private String password;

    public User(String userName, String nickName, String email, String phoneNum, String userId, String password) {
        UserVaildator.vaildateNickname(nickName);
        UserVaildator.vaildateEmail(email);
        UserVaildator.vaildatePhoneNum(phoneNum);
        UserVaildator.vaildatePassword(password);

        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.userId = userId;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.setUpdatedAt();
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        UserVaildator.vaildateNickname(nickName);
        this.setUpdatedAt();
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        UserVaildator.vaildateEmail(email);
        this.setUpdatedAt();
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        UserVaildator.vaildatePhoneNum(phoneNum);
        this.setUpdatedAt();
        this.phoneNum = phoneNum;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        UserVaildator.vaildatePassword(password);
        this.setUpdatedAt();
        this.password = password;
        System.out.println("비밀번호가 재설정되었습니다. 다시 로그인 해주세요.");
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "User{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                str +
                '}';
    }
}
