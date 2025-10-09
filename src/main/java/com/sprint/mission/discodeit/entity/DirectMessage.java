package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class DirectMessage extends BaseEntity<UUID>{

    private UUID ReceiverId;
    private UUID SenderId;
    private String Message;

    /**
     * 자식 클래스로부터 생성된 ID를 전달받는 생성자입니다.
     * ID 생성의 책임을 자식에게 위임합니다.
     */
    protected DirectMessage() {
        super(UUID.randomUUID());
    }
}
