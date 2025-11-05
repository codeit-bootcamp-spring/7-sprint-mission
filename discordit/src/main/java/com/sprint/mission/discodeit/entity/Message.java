package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.enums.ReceiverType;
import lombok.Getter;
import lombok.Setter;

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
    @Setter
    private String content;
    private final List<BinaryContent> attachments = new ArrayList<>();

    public Message(User sender, ReceiverType type, Receivable receiver,
                   String content) {
        this.sender = sender;
        this.type = type;
        this.receiver = receiver;
        this.content = content;
    }

    // 더미 데이터 타임스탬프 조정을 허용 (테스트/시드용)
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public void addAttachment(BinaryContent attachment) {
        this.attachments.add(attachment);
    }

    public void addAttachments(List<BinaryContent> attachments) {
        this.attachments.addAll(attachments);
    }

    // 더미 데이터 생성 or 파일 데이터 복원용 (createdAt, updateAt 조작 위해 엔터티 내 배치.)
    public static Message fromDto(UUID uuid, Instant createdAt, Instant updatedAt,
                                  User sender, ReceiverType type, Receivable receiver,
                                  String message, List<BinaryContent> contents) {
        Message msg = new Message(sender, type, receiver, message);
        msg.attachments.addAll(contents);
        msg.uuid = uuid;
        msg.createdAt = createdAt;
        msg.updatedAt = updatedAt;

        return msg;
    }
}