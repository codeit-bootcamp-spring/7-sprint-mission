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


  private String channelName; //채널 이름
  private String description; //채널 설명
  private ChannelType type; //채널 타입
  private final List<UUID> members; // 채널 멤버 목록


  public Channel(String channelName, String description, ChannelType type) {
    super();
    this.channelName = channelName;
    this.description = description;
    this.type = type;
    this.members = new ArrayList<>();
  }

  public Channel(String channelName, String description, ChannelType type, List<UUID> members) {
    super();
    this.channelName = channelName;
    this.description = description;
    this.type = type;
    this.members = new ArrayList<>(members);
  }

  public void updateInfo(String channelName, String description, ChannelType type) {
    this.channelName = channelName;
    this.description = description;
    this.type = type;
    update();
  }
}

//    public void addMember(UUID userId){
//        if(!members.contains(userId)){
//            members.add(userId);
//            update();
//        }
//    }
//
//    public void removeMember(UUID userId){
//        members.remove(userId);
//        update();
//    }
