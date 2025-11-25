package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.function.Function;

public class PageResponseMapper {

    /**
     * Page → PageResponse
     */
    public static <T, R> PageResponse<R> fromPage(
            Page<T> page,
            Function<T, R> mapper
    ) {
        return PageResponse.of(
                page.map(mapper).getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    /**
     * Slice → PageResponse
     * (totalElements가 없으므로 null 처리)
     */
    public static <T, R> PageResponse<R> fromSlice(
            Slice<T> slice,
            Function<T, R> mapper
    ) {
        return PageResponse.of(
                slice.map(mapper).getContent(),
                slice.getNumber(),
                slice.getSize(),
                null
        );
    }
}
