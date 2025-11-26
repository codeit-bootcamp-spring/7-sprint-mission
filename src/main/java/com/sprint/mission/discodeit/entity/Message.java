package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.subTable.MessageAttachment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseUpdatableEntity {

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id",nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "channel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;

    @OneToMany(fetch = FetchType.LAZY)
    List<BinaryContent> attachments;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MessageAttachment> messageAttachment;

    public static Message createMessageFactory(String content, User author, Channel channel){
        return new Message(content,author,channel,null,null);
    }
    /// // attachments 삭제했을때도 제대로 삭제되었는지 테스트 해봐야 함


}
