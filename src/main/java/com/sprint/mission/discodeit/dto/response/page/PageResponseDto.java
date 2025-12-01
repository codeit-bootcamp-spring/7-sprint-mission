package com.sprint.mission.discodeit.dto.response.page;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int number,
        int size,
        boolean hasNext,
        Long totalElements
){}
