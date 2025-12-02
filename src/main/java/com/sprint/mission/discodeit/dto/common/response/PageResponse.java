package com.sprint.mission.discodeit.dto.common.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

  private final List<T> content;    // 실제 데이터
  private final int number;         // 페이지 번호 (0부터 시작)
  private final int size;           // 페이지 크기
  private final boolean hasNext;    // 다음 페이지 존재 여부
  private final Long totalElements; // 총 요소 수, Slice인 경우 null 가능
}
