package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private ChannelType type;

  @Column(name = "name", length = 100)
  private String channelName;

  @Column(name = "description", length = 500)
  private String description;

  public Channel(ChannelType type, String channelName, String description) {
    this.type = type;
    this.channelName = channelName;
    this.description = description;
  }

  public void updateChannel(String channelName, String description) {
    if (channelName != null && !channelName.equals(this.channelName)) {
      this.channelName = channelName;
    }
    if (description != null && !description.equals(this.description)) {
      this.description = description;
    }
  }
}
