package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponseDtoBasic<T>(
        List<T> content,
        int number,
        int size,
        boolean hasNext,
        Long totalElements
) {
}
