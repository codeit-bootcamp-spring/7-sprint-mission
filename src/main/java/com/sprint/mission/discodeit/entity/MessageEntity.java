package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class MessageEntity extends BaseUpdatableEntity{

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private ChannelEntity channelId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}