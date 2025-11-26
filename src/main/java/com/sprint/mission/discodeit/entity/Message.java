package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity{
    private String content;

    // 채널 삭제시 관련 메시지들도 모두 삭제
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "channel_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_message_channel",
                    foreignKeyDefinition = "FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE"
            )
    )
    private Channel channel;

    // 유저 삭제시 author_id 컬럼을 null로 세팅
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "author_id",
            foreignKey = @ForeignKey(
                    name = "fk_message_user",
                    foreignKeyDefinition = "FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL"
            )
    )
    private User author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(
                    name = "message_id",
                    nullable = false,
                    foreignKey = @ForeignKey(name = "fk_message_attachments_message")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "attachment_id",
                    nullable = false,
                    foreignKey = @ForeignKey(name = "fk_message_attachments_binary_content")
            )
    )
    private List<BinaryContent> attachments;

    public void update(String content) {
        if (content != null && !content.equals(this.content)) {
            this.content = content;
        }
    }

    @Override
    public String toString() {
        String str = super.toString();
        return "Message{" +
                "contents='" + content + '\'' +
                "attachments=" + attachments +
                str +
                '}';
    }
}
