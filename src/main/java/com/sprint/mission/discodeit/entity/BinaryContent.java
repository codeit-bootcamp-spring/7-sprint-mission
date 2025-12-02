package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

  // 파일 이름
  @Column(name = "file_name", nullable = false)
  private String fileName;

  // 파일 사이즈
  @Column(name = "size", nullable = false)
  private Long size;

  // 파일 형식
  @Column(name = "content_type", nullable = false, length = 100)
  private String fileType;

  //Constructor
  private BinaryContent(String fileName, String fileType, long size) {

    this.fileName = fileName;
    this.fileType = fileType;
    this.size = size;
  }

  //Factory Method
  public static BinaryContent create(String fileName, String fileType, long size) {
    return new BinaryContent(fileName, fileType, size);
  }
}
