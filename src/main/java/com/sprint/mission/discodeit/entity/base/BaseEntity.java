package com.sprint.mission.discodeit.entity.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@NoArgsConstructor
public abstract class BaseEntity {

  private UUID id;

  @CreatedDate
  private Instant createdAt;


}