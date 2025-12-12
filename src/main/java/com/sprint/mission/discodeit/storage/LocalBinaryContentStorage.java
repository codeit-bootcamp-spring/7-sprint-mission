package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private Path root;

  public LocalBinaryContentStorage(BinaryContentRepository binaryContentRepository,
      @Value("${discodeit.storage.local.root-path}")
      Path root) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    if (Files.notExists(root)) {
      log.info("저장 디렉토리 생성 - 경로: {}", root);
      try {
        Files.createDirectories(root);
        log.info("저장 디렉토리 생성 완료 - 경로: {}", root);
      } catch (IOException e) {
        log.error("저장소 초기화 실패 - 경로: {}", root, e);
        throw new RuntimeException("초기화 실패: " + root, e);
      }
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path path = resolvePath(binaryContentId);

    log.debug("파일 저장 시작 - binaryContentId: {}", binaryContentId);

    try {
      Files.createDirectories(path.getParent());
      Files.write(path, bytes);
      log.debug("파일 저장 완료 - binaryContentId: {}", binaryContentId);

    } catch (IOException e) {
      log.error("파일 저장 실패 - binaryContentId: {}", binaryContentId, e);
      throw new RuntimeException("파일 쓰기 실패: " + e);
    }
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path path = resolvePath(binaryContentId);

    log.debug("파일 읽기 시작 - binaryContentId: {}", binaryContentId);

    InputStream input;
    try {
      input = Files.newInputStream(path);
      log.debug("파일 읽기 완료 - binaryContentId: {}", binaryContentId);
    } catch (IOException e) {
      log.error("파일 읽기 실패 - binaryContentId: {}", binaryContentId, e);
      throw new RuntimeException("파일 가져오기 실패: " + e);
    }
    return input;
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentResponseDto responseDto) {
    InputStream inputStream = get(responseDto.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(responseDto.contentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment()
                .filename(responseDto.fileName())
                .build()
                .toString())
        .body(resource);
  }

  private Path resolvePath(UUID binaryContentId) {
    String idString = binaryContentId.toString();
    return root.resolve(idString);
  }
}
