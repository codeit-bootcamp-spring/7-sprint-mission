package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "messages")
public class MessageEntity extends BaseUpdatableEntity{

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChannelEntity channelId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
