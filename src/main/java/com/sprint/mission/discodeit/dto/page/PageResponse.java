package com.sprint.mission.discodeit.dto.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public record PageResponse<T>(List<T> content,
                              int number,
                              int size,
                              boolean hasNext,
                              Long totalElements
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }

    public static <T> PageResponse<T> from(Slice<T> slice) {
        return new PageResponse<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                null
        );
    }
}

