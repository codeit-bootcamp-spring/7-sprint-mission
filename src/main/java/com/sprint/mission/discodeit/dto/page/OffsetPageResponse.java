package com.sprint.mission.discodeit.dto.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public record OffsetPageResponse<T>(List<T> content,
                                    int number,
                                    int size,
                                    boolean hasNext,
                                    Long totalElements
) {
    public static <T> OffsetPageResponse<T> from(Page<T> page) {
        return new OffsetPageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }

    public static <T> OffsetPageResponse<T> from(Slice<T> slice) {
        return new OffsetPageResponse<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                null
        );
    }
}

