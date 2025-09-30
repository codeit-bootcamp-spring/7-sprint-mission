package com.sprint.mission.discodeit.entity.update;

public class UserUpdate {

    private String password;
    private String userName;
    private String phoneNum;

    public UserUpdate() {}   //빌더 중 자바빈즈 패턴

    public String getPassword() {
        return password;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void updatePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
