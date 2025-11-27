package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @ToString
@Entity
@Table(name = "messages")
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

    @Column(columnDefinition = "TEXT")
    private String content;

    @JoinColumn(name = "channel_id",  nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Channel channel;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id"))
    private List<BinaryContent> attachments = new ArrayList<>();

    @Builder
    public Message(User author, Channel channel, String content, List<BinaryContent> attachments) {
        this.content = content;
        this.channel = channel;
        this.author = author;
        if (attachments != null) {
            this.attachments.addAll(attachments);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }
}



