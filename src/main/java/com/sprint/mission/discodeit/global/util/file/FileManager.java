package com.sprint.mission.discodeit.global.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileManager {

  // TODO: CustomException
  private FileManager() {
  }

  public static void init(Path root) {
    try {
      if (Files.notExists(root)) {
        Files.createDirectories(root);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to initialize storage directory: " + root, e);
    }
  }

  public static void write(Path path, byte[] bytes) {
    try {
      Files.createDirectories(path.getParent());
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to write file: " + path, e);
    }
  }

  public static InputStream read(Path path) {
    try {
      return Files.newInputStream(path);
    } catch (IOException e) {
      throw new UncheckedIOException("파일 읽기 실패: " + path, new IOException(e));
    }
  }
}

