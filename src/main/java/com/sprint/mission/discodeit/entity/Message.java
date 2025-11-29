package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @ManyToMany
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_msg_attachment_msg")),
      inverseJoinColumns = @JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "fk_msg_attachment_file"))
  )
  private List<BinaryContent> attachments = new ArrayList<>();

  //Constructor
  private Message(Channel channel, User speaker,
      String content, List<BinaryContent> attachments) {
    this.channel = channel;
    this.speaker = speaker;
    this.content = content;
  }

  //Factory
  public static Message create(Channel channel, User speaker, String content,
      List<BinaryContent> attachments) {
    return new Message(channel, speaker, content, attachments);
  }

  //메세지 수정
  public Message update(String content) {
    super.update();
    this.content = content;
    return this;
  }
}
