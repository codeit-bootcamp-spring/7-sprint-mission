package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.common.response.PageResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PageResponseMapper {

  // Slice -> PageResponse
  public static <T> PageResponse<T> fromSlice(Slice<T> slice) {
    List<T> content = slice.getContent();
    int number = slice.getNumber();
    int size = slice.getSize();
    boolean hasNext = slice.hasNext();

    return new PageResponse<>(content, number, size, hasNext, null);
  }

  // Page -> PageResponse
  public static <T> PageResponse<T> fromPage(Page<T> page) {
    List<T> content = page.getContent();
    int number = page.getNumber();
    int size = page.getSize();
    boolean hasNext = page.hasNext();
    long totalElements = page.getTotalElements();

    return new PageResponse<>(content, number, size, hasNext, totalElements);
  }
}
