package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id; // ID
  private final Instant createdAt; // 생성 시간

  private final byte[] data; //표준 데이터
  private final String fileName; // 파일 이름
  private final String fileType; // 파일 타입


  public BinaryContent(byte[] data, String fileName, String fileType) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.data = data;
    this.fileName = fileName;
    this.fileType = fileType;
  }
}
