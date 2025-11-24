package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
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
