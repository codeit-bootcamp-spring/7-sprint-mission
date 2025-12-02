package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local")
@RequiredArgsConstructor
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (Exception e) {
      throw new RuntimeException("로컬 storage 루트 경로 생성 실패: " + root, e);
    }
  }

  // 저장
  @Override
  public UUID put(UUID binaryId, byte[] data) {
    try {
      UUID id = (binaryId != null) ? binaryId : UUID.randomUUID();
      Path filePath = resolvePath(id);
      Files.write(filePath, data);
      return id;
    } catch (Exception e) {
      throw new RuntimeException("파일 저장 실패", e);
    }
  }

  // 조회
  @Override
  public InputStream get(UUID binaryId) {
    try {
      Path filePath = resolvePath(binaryId);
      if (!Files.exists(filePath)) {
        throw new RuntimeException("파일 없음: " + binaryId);
      }
      return Files.newInputStream(filePath);
    } catch (Exception e) {
      throw new RuntimeException("파일 읽기 실패", e);
    }
  }

  // 다운로드
  @Override
  public ResponseEntity<Resource> download(BinaryContentInfoRes dto) {
    try {
      Path filePath = resolvePath(dto.binaryContentId());
      if (!Files.exists(filePath)) {
        throw new RuntimeException("파일 없음: " + dto.binaryContentId());
      }

      Resource resource = new FileSystemResource(filePath);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + dto.fileName() + "\"")
          .contentType(MediaType.parseMediaType(dto.fileType()))
          .body(resource);
    } catch (Exception e) {
      throw new RuntimeException("파일 다운로드 실패", e);
    }
  }

  // 삭제
  @Override
  public void delete(UUID binaryId) {
    try {
      Path filePath = resolvePath(binaryId);
      if (!Files.exists(filePath)) {
        throw new RuntimeException("삭제할 파일 없음: " + binaryId);
      }
      Files.delete(filePath);
    } catch (Exception e) {
      throw new RuntimeException("파일 삭제 실패: " + binaryId, e);
    }
  }

  // 내부 유틸: UUID → Path
  private Path resolvePath(UUID binaryId) {
    return root.resolve(binaryId.toString());
  }
}
