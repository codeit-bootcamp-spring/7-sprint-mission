package com.sprint.mission.discodeit.entity.vaildator;

public class UserVaildator {
    private static final String NICKNAME_PATTERN = "^[가-힣a-zA-Z0-9]+$";
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    public static void vaildateNickname(String nickName){
        if(nickName.length() < 2 || nickName.length() > 12){
            throw new IllegalArgumentException("닉네임을 2자 이상 12자 이하로 입력해주세요.");
        } else if(!nickName.matches(NICKNAME_PATTERN)){
            throw new IllegalArgumentException("닉네임에 특수문자는 사용할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public static void vaildateEmail(String email){
        if(!email.matches(EMAIL_PATTERN)){
            throw new IllegalArgumentException("이메일 형식에 맞지 않습니다. 다시 입력하세요");
        }
    }

    public static void vaildatePhoneNum(String phoneNum){
        phoneNum = phoneNum.replaceAll("[^0-9]", ""); //숫자를 제외한 나머지 문자열 삭제

        if (phoneNum.matches("^010\\d{8}$")) {
            phoneNum = phoneNum.replaceFirst("^(010)(\\d{4})(\\d{4})$", "$1-$2-$3");
        }
        else if (phoneNum.matches("^01[1-9]\\d{7}$")) { // 10자리 번호 (일부 구형 011, 016 등)
            phoneNum =  phoneNum.replaceFirst("^(01[1-9])(\\d{3,4})(\\d{4})$", "$1-$2-$3");
        } else{
            throw new IllegalArgumentException("전화번호의 형식에 맞지 않습니다. 다시 입력하세요");
        }
    }

    public static void vaildatePassword(String password){
        if(password.length() < 8 || password.isBlank()){
            throw new IllegalArgumentException("비밀번호가 8자리보다 작거나, 공백만 포함되어 있습니다. 다시 입력해주세요.");
        }
    }

}
