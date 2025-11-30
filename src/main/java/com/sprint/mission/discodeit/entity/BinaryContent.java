package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

  private static final long serialVersionUID = 1L;

  // 파일 이름
  @Column(name = "file_name", nullable = false)
  private String fileName;

  // 파일 사이즈
  @Column(name = "size", nullable = false)
  private Long size;

  // 파일 형식
  @Column(name = "content_type", nullable = false, length = 100)
  private String fileType;

  // 파일 데이터
  @JdbcTypeCode(SqlTypes.VARBINARY)
  @Column(name = "bytes", nullable = false)
  private byte[] data;

  //Constructor
  private BinaryContent(byte[] data, String fileName, String fileType) {
    this.data = data;
    this.fileName = fileName;
    this.fileType = fileType;
    this.size = (long) (data == null ? 0 : data.length);
  }

  //Factory Method
  public static BinaryContent create(byte[] data, String fileName, String fileType) {
    return new BinaryContent(data, fileName, fileType);
  }
}
