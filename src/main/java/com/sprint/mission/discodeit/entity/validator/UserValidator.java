package com.sprint.mission.discodeit.entity.validator;

import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;

public class UserValidator {
    private static final String NICKNAME_PATTERN = "^[가-힣a-zA-Z0-9]+$";
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    public static void validateNickname(String nickName){
        ValidationUtils.validateNotNull(nickName); // 입력 값이 null인지 검증

        if(nickName.length() < 2 || nickName.length() > 12){
            throw new CustomException(ErrorCode.INVALID_NICKNAME_LENGTH);
        } else if(!nickName.matches(NICKNAME_PATTERN)){
            throw new CustomException(ErrorCode.INVALID_NICKNAME_PATTERN);
        }
    }

    public static void validateEmail(String email){
        ValidationUtils.validateNotNull(email);

        if(!email.matches(EMAIL_PATTERN)){
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    public static void validatePhoneNum(String phoneNum){
        ValidationUtils.validateNotNull(phoneNum);
        phoneNum = phoneNum.replaceAll("[^0-9]", ""); //숫자를 제외한 나머지 문자열 삭제

        if (!phoneNum.matches("^010\\d{8}$") && !phoneNum.matches("^01[1-9]\\d{7}$")){
            throw new CustomException(ErrorCode.INVALID_PHONE_FORMAT);
        }
    }

    public static void validatePassword(String password){
        ValidationUtils.validateNotNull(password);

        if(password.length() < 8 || password.isBlank()){
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);        }
    }
}
