package com.sprint.mission.discodeit.loggin;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Component
public class MultipartFileSanitizer implements ArgSanitizer {

  @Override
  public boolean isFilterCase(Object arg) {
    if (arg instanceof MultipartFile) {
      return true;
    }
    if (arg instanceof List<?> list && list.stream().anyMatch(i -> i instanceof MultipartFile)) {
      return true;
    }
    return false;
  }

  @Override
  public Object sanitize(Object arg) {
    if (arg instanceof MultipartFile file && !file.isEmpty()) {
      return Map.of(
          "fileName", file.getOriginalFilename(),
          "size", file.getSize(),
          "contentType", file.getContentType()
      );
    }
    if (arg instanceof List<?> list) {
      return list.stream().map(this::sanitize).toList();
    }
    return arg;
  }
}
