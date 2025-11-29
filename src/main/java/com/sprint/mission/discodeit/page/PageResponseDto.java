package com.sprint.mission.discodeit.page;

import java.util.List;

public record PageResponseDto<T>(
    List<T> content,
    int number,
    int size,
    boolean hasNext,
    Long totalElements) {
}
