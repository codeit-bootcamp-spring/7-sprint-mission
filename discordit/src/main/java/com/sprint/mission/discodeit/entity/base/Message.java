package com.sprint.mission.discodeit.entity.base;

import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.enums.ReceiverType;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {

    // 직렬화 및 역직렬화를 수행할 때 이 클래스의 버전을 의미
    private static final long serialVersionID = 1L;

    private final User sender;
    private final ReceiverType type;
    private final Receivable receiver;
    private final String message;
    private final List<BinaryContent> attachments = new ArrayList<>();

    public Message(User sender, ReceiverType type, Receivable receiver,
                   String message, List<BinaryContent> attachments) {
        this.sender = sender;
        this.type = type;
        this.receiver = receiver;
        this.message = message;
        this.attachments.addAll(attachments);
    }

    // 더미 데이터 생성 or 파일 데이터 복원용 (createdAt, updateAt 조작 위해 엔터티 내 배치.)
    public static Message fromDto(UUID uuid, Instant createdAt, Instant updatedAt,
                                  User sender, ReceiverType type, Receivable receiver, String message, List<BinaryContent> contents) {
        Message msg = new Message(sender, type, receiver, message, contents);
        msg.uuid = uuid;
        msg.createdAt = createdAt;
        msg.updatedAt = updatedAt;

        return msg;
    }
}