package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public enum ChannelMemberRole {
  MANAGER("manager"),
  MEMBER("member");

  private final String value;

  ChannelMemberRole(String value) {
    this.value = value;
  }
}
