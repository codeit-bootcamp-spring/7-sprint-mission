package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseEntity{
    private String realName;
    private String nickName;
    private String email;
    private String phoneNum;
    private String username;
    private String password;
    private UUID profileId;

    public User(String realName, String nickName, String email, String phoneNum, String username, String password) {
        this.realName = realName;
        this.nickName = nickName;
        this.email = email;
        this.phoneNum = formatPhoneNum(phoneNum);
        this.username = username;
        this.password = password;
    }

    public User(String realName, String nickName, String email, String phoneNum, String username, String password, UUID profileId) {
        this(realName, nickName, email, phoneNum, username, password);
        this.profileId = profileId;
    }

    public void update(String realName, String nickName, String email, String phoneNum, String username, String password, UUID profileId) {
        boolean flag = false;

        if (realName != null && !realName.equals(this.realName)) {
            this.realName = realName;
            flag = true;
        }
        if (nickName != null && !nickName.equals(this.nickName)) {
            this.nickName = nickName;
            flag = true;
        }
        if (email != null && !email.equals(this.email)) {
            this.email = email;
            flag = true;
        }
        if (phoneNum != null && !phoneNum.equals(this.phoneNum)) {
            this.phoneNum = formatPhoneNum(phoneNum);
            flag = true;
        }
        if (username != null && !username.equals(this.username)) {
            this.username = username;
            flag = true;
        }
        if (password != null && !password.equals(this.password)) {
            this.password = password;
            flag = true;
        }
        if (profileId != null && !profileId.equals(this.profileId)) {
            this.profileId = profileId;
            flag = true;
        }
        if(flag) {
            this.setUpdatedAt();
        }
    }

    private String formatPhoneNum(String phoneNum){
        phoneNum = phoneNum.replaceAll("[^0-9]", ""); //숫자를 제외한 나머지 문자열 삭제
        if (phoneNum.matches("^010\\d{8}$")) {
            phoneNum = phoneNum.replaceFirst("^(010)(\\d{4})(\\d{4})$", "$1-$2-$3");
        }
        else if (phoneNum.matches("^01[1-9]\\d{7}$")) { // 10자리 번호 (일부 구형 011, 016 등)
            phoneNum = phoneNum.replaceFirst("^(01[1-9])(\\d{3,4})(\\d{4})$", "$1-$2-$3");
        } else {
            throw new IllegalArgumentException("전화번호 형식이 맞지 않습니다.");
        }
        return phoneNum;
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "User{" +
                "realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", userId='" + username + '\'' +
                ", password='" + password + '\'' +
                str +
                '}';
    }
}
