package com.sprint.mission.discodeit.dto;

import java.util.List;

/***
 * @param content 실제 데이터
 * @param nextCursor 페이지 번호
 * @param size 페이지의 크기
 * @param totalElements T 데이터의 총 개수, null일 수 있음
 */
public record PageResponse<T>(
    List<T> content,
    Object nextCursor,
    int size,
    boolean hasNext,
    Long totalElements
) {

}
