package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

  private String content; // 메시지
  private UUID channelId; // 채널ID
  private UUID authorId; // 유저ID
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
  }
}
