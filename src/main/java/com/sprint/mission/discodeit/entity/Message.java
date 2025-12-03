package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
@AttributeOverride(name = "id", column = @Column(name = "message_id"))
public class Message extends BaseUpdatableEntity {

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public void updateContent(String content){
        this.content = content;
    }
}