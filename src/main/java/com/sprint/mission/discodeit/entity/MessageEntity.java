package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Setter
@Entity
@Table(name = "messages")
public class MessageEntity extends BaseUpdatableEntity{


    @Column(name="user_id", nullable = false)
    private UUID userId;


    @Column(name = "channel_id", nullable = false)
    private UUID channelId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}