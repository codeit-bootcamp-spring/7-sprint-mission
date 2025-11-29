package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  private static final long serialVersionUID = 1L;

  //Field
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_msg_channel"))
  private Channel channel;             //채널

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_msg_author"))
  private User speaker;             //화자 UUID

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;                   //메세지 내용

  //Constructor
  private Message(Channel channel, User speaker,
      String content) {
    this.channel = channel;
    this.speaker = speaker;
    this.content = content;
  }

  //Factory
  public static Message create(Channel channel, User speaker, String content) {
    return new Message(channel, speaker, content);
  }

  //메세지 수정
  public Message update(String content) {
    super.update();
    this.content = content;
    return this;
  }
}
