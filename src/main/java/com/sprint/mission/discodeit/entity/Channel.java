package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  private static final long serialVersionUID = 1L;

  //Field
  // 채널명
  @Column(name = "name", length = 100)
  private String name;

  // 채널 설명
  @Column(name = "description", length = 500)
  private String description;

  // 생성자 UUID
  @Column(name = "manager_id", nullable = false)
  private UUID managerId;

  // 공개/비공개 타입
  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 10, nullable = false)
  private ChannelType publicType;

  @Transient
  private List<UUID> userIds;               //채널 참가자


  //Constructor
  private Channel(UUID managerId, String name, String description, List<UUID> userIds,
      ChannelType publicType) {
    this.managerId = managerId;
    this.name = name;
    this.description = description;
    this.userIds = userIds;
    this.publicType = publicType;
  }

  //Factory Method
  public static Channel createPublic(UUID managerId, String name, String description) {
    return new Channel(
        managerId,
        name,
        description,
        null,
        ChannelType.PUBLIC
    );
  }

  public static Channel createPrivate(UUID managerId, List<UUID> userIds) {
    String privateName = "비밀방" + UUID.randomUUID().toString().replace("-", "");
    return new Channel(
        managerId,
        privateName,
        null,
        userIds,
        ChannelType.PRIVATE
    );
  }

  //update name
  public Channel update(String name, String description) {
    super.update();
    this.name = name;
    this.description = description;
    return this;
  }
}
