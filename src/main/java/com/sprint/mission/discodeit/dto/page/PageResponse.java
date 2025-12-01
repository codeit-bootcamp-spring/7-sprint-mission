package com.sprint.mission.discodeit.dto.page;

import java.util.List;

public record PageResponse<T>(List<T> content,
                              Object nextCursor,
                              int size,
                              boolean hasNext,
                              Long totalElements
) {
    public static <T> PageResponse<T> from(
            List<T> content, Object nextCursor, int size, boolean hasNext, Long totalElements)
    {
        return new PageResponse<>(
                content,
                nextCursor,
                size,
                hasNext,
                totalElements
        );
    }
}
