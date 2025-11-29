package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.page.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {
    public <T> PageResponseDto<T> fromSlice(Slice<T> slice) {
        return new PageResponseDto<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                null
        );
    }
    public <T> PageResponseDto<T> fromPage(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }
}
