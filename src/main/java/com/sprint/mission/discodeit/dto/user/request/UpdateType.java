package com.sprint.mission.discodeit.dto.user.request;

public enum UpdateType {
    USER_NAME("이름"),
    NICK_NAME("닉네임"),
    EMAIL("이메일"),
    PASSWORD("비밀번호"),
    PHONE_NUM("전화번호"),
    PROFILE("프로필");

    private final String desc;

    UpdateType(String desc) {
        this.desc = desc;
    }
}
