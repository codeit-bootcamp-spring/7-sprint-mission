package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.config.enums.ContentOwner;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
public class BinaryContent extends BaseEntity<UUID> {

  private final UUID ownerId;
  private final ContentOwner owner;
  private String fileName;
  private String filePath;
  private String fileType;
  private Long fileSize;
  private String bytes;


  private BinaryContent(UUID ownerId, ContentOwner owner) {
    super(UUID.randomUUID());
    if (ownerId == null) {
      throw new IllegalArgumentException("ownerId가 없습니다.");
    }
    if (owner == null) {
      throw new IllegalArgumentException("ownerType을 확인 할 수 없습니다.");
    }
    this.ownerId = ownerId;
    this.owner = owner;
  }

  public static BinaryContent create(UUID ownerId, ContentOwner owner, String fileName,
      String fileType, Long fileSize, String bytes) {
    if (fileName == null || fileName.isBlank()) {
      throw new IllegalArgumentException("파일 이름을 확인 할 수 없습니다.");
    }
    if (fileType == null || fileType.isBlank()) {
      throw new IllegalArgumentException("파일 타입을 확인 할 수 없습니다.");
    }
    if (fileSize < 0) {
      throw new IllegalArgumentException("파일 용량이 식별되지 않습니다.");
    }
    if (ownerId == null) {
      throw new IllegalArgumentException("파일 개시자를 확인 할 수 없습니다.");
    }
    if (owner == null) {
      throw new IllegalArgumentException("파일 개시자 정보를 확인 할 수 없습니다.");
    }

    BinaryContent content = new BinaryContent(ownerId, owner);
    content.fileName = fileName;
    content.fileType = fileType;
    content.fileSize = fileSize;
    content.bytes = bytes;
    return content;
  }

  public void updateStoragePath(String newPath) {
    if (this.filePath != null && !this.filePath.isBlank()) {
      throw new IllegalStateException("스토리지 경로는 한 번만 설정할 수 있습니다.");
    }

    if (newPath == null || newPath.isBlank()) {
      throw new IllegalArgumentException("저장 경로는 null일 수 없습니다.");
    }
    this.filePath = newPath;
  }
}
