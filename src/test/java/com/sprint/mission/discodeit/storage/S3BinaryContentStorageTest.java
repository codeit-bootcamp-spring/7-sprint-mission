package com.sprint.mission.discodeit.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class S3BinaryContentStorageTest {

  private S3BinaryContentStorage storage;

  @BeforeEach
  void setUp() throws IOException {
    Properties properties = new Properties();
    InputStream inputStream = Files.newInputStream(Path.of(".env"));
    properties.load(inputStream);

    String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    String region = properties.getProperty("AWS_S3_REGION");
    String bucket = properties.getProperty("AWS_S3_BUCKET");

    storage = new S3BinaryContentStorage(accessKey, secretKey, region, bucket);
  }

  @Test
  @DisplayName("파일 업로드")
  void put() {
    // given
    UUID id = UUID.randomUUID();
    byte[] data = "File test".getBytes();

    // when
    UUID savedId = storage.put(id, data);

    // then
    assertEquals(id, savedId);
    log.info("업로드 성공: {}", id);
  }

  @Test
  @DisplayName("파일 다운로드")
  void get() throws IOException {
    // given
    UUID id = UUID.randomUUID();
    byte[] data = "File test".getBytes();
    storage.put(id, data);

    // when
    InputStream inputStream = storage.get(id);
    byte[] bytes = inputStream.readAllBytes();

    // then
    assertArrayEquals(data, bytes);
    log.info("다운로드 성공: {}", id);
  }

}