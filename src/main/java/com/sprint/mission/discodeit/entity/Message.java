package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BasicEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String content; // 메시지
  private final UUID channelId; // 채널ID
  private final UUID authorId; // 유저ID
  private List<UUID> attachmentIds; // 첨부파일


  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    super();
    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;
    this.attachmentIds = attachmentIds;
  }

  public void updateContent(String newContent) {
    this.content = newContent;
    update();
  }
}
