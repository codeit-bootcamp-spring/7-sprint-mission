package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
public class Message extends BaseUpdatableEntity {

    @Column(name = "content")
    private String content;

    @JoinColumn(name = "channel_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Channel channel;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message", orphanRemoval = true, cascade = CascadeType.REMOVE)
//    private List<MessageAttachments> messageAttachmentList;

//    public Message(String content, Channel channel, User author, List<MessageAttachments> attachmentId) {
//        this.content = content;
//        this.channel = channel;
//        this.author = author;
//        this.attachmentId = attachmentId;
//    }
}
