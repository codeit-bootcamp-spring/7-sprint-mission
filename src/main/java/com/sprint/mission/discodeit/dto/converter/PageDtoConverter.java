package com.sprint.mission.discodeit.dto.converter;

import com.sprint.mission.discodeit.dto.page.Response.PageResponseDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class PageDtoConverter {
    public <T> PageResponseDto<T> toResponseDto(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }
}
