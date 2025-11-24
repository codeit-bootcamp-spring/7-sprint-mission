package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

  private String fileName; // 파일 이름
  private Long size;
  private String contentType;
  private byte[] bytes;


  public BinaryContent(String fileName, String contentType, byte[] bytes) {
    this.fileName = fileName;
    this.size = (long) bytes.length;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
