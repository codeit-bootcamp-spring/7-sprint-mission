package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.vaildator.UserVaildator;
import lombok.Getter;

@Getter
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
        this.phoneNum = formatPhoneNum(phoneNum);
        this.userId = userId;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.setUpdatedAt();
        this.userName = userName;
    }

    public void setNickName(String nickName) {
        UserVaildator.vaildateNickname(nickName);
        this.setUpdatedAt();
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        UserVaildator.vaildateEmail(email);
        this.setUpdatedAt();
        this.email = email;
    }

    public void setPhoneNum(String phoneNum) {
        UserVaildator.vaildatePhoneNum(phoneNum);
        this.setUpdatedAt();
        this.phoneNum = formatPhoneNum(phoneNum);
    }

    public void setPassword(String password) {
        UserVaildator.vaildatePassword(password);
        this.setUpdatedAt();
        this.password = password;
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
