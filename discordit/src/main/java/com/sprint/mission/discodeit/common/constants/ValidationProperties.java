package com.sprint.mission.discodeit.common.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidationProperties {
    public static int ID_MIN_LENGTH;
    public static int ID_MAX_LENGTH;
    public static int PASSWD_MIN_LENGTH;
    public static int PASSWD_MAX_LENGTH;
    public static int DISPLAY_NAME_MIN_LENGTH;
    public static int DISPLAY_NAME_MAX_LENGTH;
    public static String EMAIL_REGEX;

    @Value("${validation.user.id.min-length}")
    public void setIdMinLength(int value) {
        ID_MIN_LENGTH = value;
    }

    @Value("${validation.user.id.max-length}")
    public void setIdMaxLength(int value) {
        ID_MAX_LENGTH = value;
    }

    @Value("${validation.user.password.min-length}")
    public void setPasswdMinLength(int value) {
        PASSWD_MIN_LENGTH = value;
    }

    @Value("${validation.user.password.max-length}")
    public void setPasswdMaxLength(int value) {
        PASSWD_MAX_LENGTH = value;
    }

    @Value("${validation.user.display-name.min-length}")
    public void setDisplayNameMinLength(int value) {
        DISPLAY_NAME_MIN_LENGTH = value;
    }

    @Value("${validation.user.display-name.max-length}")
    public void setDisplayNameMaxLength(int value) {
        DISPLAY_NAME_MAX_LENGTH = value;
    }

    @Value("${validation.user.email-regex}")
    public void setEmailRegex(String value) {
        EMAIL_REGEX = value;
    }
}
