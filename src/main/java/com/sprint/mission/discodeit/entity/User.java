package com.sprint.mission.discodeit.entity;


public class User extends BaseEntity{
    private String userName;
    private String nickName;
    private String email;
    private String phoneNum;
    private final String userId;
    private String password;

    public User(String userName, String nickName, String email, String phoneNum, String userId, String password) {
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
        String regex = "^[가-힣a-zA-Z0-9]+$";

        if(nickName.length() < 2 || nickName.length() > 12){
            System.out.println("닉네임을 2자 이상 12자 이하로 입력해주세요.");
            return;
        } else if(!nickName.matches(regex)){
            System.out.println("닉네임에 특수문자는 사용할 수 없습니다. 다시 입력해 주세요.");
            return;
        }
        this.setUpdatedAt();
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
         if(!email.matches(regex)){
             System.out.println("이메일 형식에 맞지 않습니다. 다시 입력하세요");
             return;
         }

        this.setUpdatedAt();
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        phoneNum = phoneNum.replaceAll("[^0-9]", ""); //숫자를 제외한 나머지 문자열 삭제

        if (phoneNum.matches("^010\\d{8}$")) {
            phoneNum = phoneNum.replaceFirst("^(010)(\\d{4})(\\d{4})$", "$1-$2-$3");
        }
        // 10자리 번호 (일부 구형 011, 016 등)
        else if (phoneNum.matches("^01[1-9]\\d{7}$")) {
            phoneNum =  phoneNum.replaceFirst("^(01[1-9])(\\d{3,4})(\\d{4})$", "$1-$2-$3");
        } else{
            System.out.println("전화번호의 형식에 맞지 않습니다. 다시 입력하세요");
            return;
        }

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
        if(password.length() < 8 || password.isBlank()){
            System.out.println("비밀번호가 8자리보다 작거나, 공백만 포함되어 있습니다. 다시 입력해주세요.");
            return;
        }

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
