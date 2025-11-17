package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.enum_.ChannelType;
import java.io.Serializable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BasicEntity implements Serializable {

  private static final long serialVersionUID = 1L;


  private ChannelType type; //채널 타입
  private String name; //채널 이름
  private String description; //채널 설명


  public Channel(ChannelType type, String name, String description) {
    super();
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void updateInfo(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
    update();
  }
}
