package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.enum_.ChannelType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

  private ChannelType type; //채널 타입
  private String name; //채널 이름
  private String description; //채널 설명

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void updateInfo(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }
}
