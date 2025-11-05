package com.sprint.mission.discodeit.config.enums;

/**
 * 사용자의 다양한 상태를 나타내는 열거형(Enum)입니다.
 * 각 상태는 화면에 표시될 수 있는 한글 설명(description)을 포함합니다.
 */
public enum Status {

    /**
     * 사용자가 현재 접속하여 활동 중인 상태입니다.
     */
    ONLINE("온라인"),

    /**
     * Away From Keyboard (AFK): 사용자가 접속 중이지만 자리를 비운 상태입니다.
     */
    AFK("자리비움"),

    /**
     * Do Not Disturb (DND): 사용자가 접속 중이지만 알림을 받지 않기를 원하는 상태입니다.
     */
    DND("방해금지"),

    /**
     * 사용자가 접속해있지 않은 상태입니다.
     */
    OFFLINE("오프라인");

    /**
     * 각 열거형 상수가 가질 한글 설명을 저장하는 필드입니다.
     * `private final`로 선언하여 불변성을 보장합니다.
     */
    private final String description;

    /**

     * 열거형 상수가 생성될 때, 해당하는 설명을 필드에 주입하는 생성자입니다.
     * enum의 생성자는 항상 private입니다.
     * @param description 각 상태에 대한 한글 설명
     */
    Status(String description) {
        this.description = description;
    }

    /**
     * 상태에 대한 한글 설명을 반환합니다.
     * @return 상태 설명 문자열 (예: "온라인")
     */
    public String getDescription() {
        return description;
    }
}