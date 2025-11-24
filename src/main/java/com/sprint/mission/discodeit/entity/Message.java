package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {
    @Column(name = "content", length = 4000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id"))
    private final List<BinaryContent> attachments = new ArrayList<>();

//    @Column(name = "is_deleted", nullable = false)
    @Transient
    private boolean isDeleted;

    protected Message() {}

    public Message(@Nonnull String content, User author, Channel channel, List<BinaryContent> attachments) {
        this.content = Objects.requireNonNull(content);
        this.author = Objects.requireNonNull(author);
        this.channel = Objects.requireNonNull(channel);
        if(attachments != null) {
            this.attachments.addAll(attachments);
        }
        this.isDeleted = false;
    }

    public void setContent(@Nonnull String content) {
        if(isDeleted){ throw new IllegalStateException("Cannot set content on deleted Message"); }
        if(!content.equals(this.content)){
            this.content = content;
        }
    }

    public void setAttachments(List<BinaryContent> attachments) {
        Objects.requireNonNull(this.attachments);
        this.attachments.clear();
        this.attachments.addAll(attachments);
    }

    public boolean delete() {
        if(!isDeleted){
            isDeleted = true;
            return true;
        }
        return false;
    }

    public String getDisplayText() {
        if(isDeleted){
            return "Deleted Message";
        } else {
            return content;
        }
    }
}
