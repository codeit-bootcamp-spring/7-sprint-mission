package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id; // ID
  private Instant createdAt; // 생성 시간

  private String fileName; // 파일 이름
  private Long size;
  private String contentType;
  private byte[] bytes;


  public BinaryContent(String fileName, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.size = (long) bytes.length;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
