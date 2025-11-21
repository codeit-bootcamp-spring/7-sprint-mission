package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
public class Message extends BaseUpdateEntity {
    private static final long serialVersionUID = 1L;

    private UUID authorId;
    private UUID channelId;
    private String content;
    private List<UUID> attachmentIds;

    public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
        this.attachmentIds = attachmentIds;
    }


    @Override
    public String toString() {
        return "Message{" +
                "채널 UUID =" + channelId +
                ",발신자 UUID  =" + authorId +
                ", 생성시간 =" + this.getCreatedAt() +
                ",  내용 ='" + content + '\'' +
                '}';
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }


    }
    //첨부파일추가
   /* public void addAttachmentId(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
    }*/
}
