package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        Long totalElements // Slice라면 null 가능
) {

    public static <T> PageResponse<T> of(
            List<T> content,
            int number,
            int size,
            Long totalElements
    ) {
        return new PageResponse<>(content, number, size, totalElements);
    }
}
