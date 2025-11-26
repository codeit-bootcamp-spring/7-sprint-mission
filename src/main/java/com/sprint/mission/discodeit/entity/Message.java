package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

    @Column(name = "content")
    private String content;

    @JoinColumn(name = "channel_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Channel channel;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message_id")
    private List<BinaryContent> attachments; //??
}
