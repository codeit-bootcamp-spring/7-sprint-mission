package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDtoBasic;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageResponseBasicMapper<T> {

    public PageResponseDtoBasic<T> fromPage(Page<T> page){
        List<T> pageContent = page.getContent();
        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        boolean hasNext = page.hasNext();
        Long totalElements = page.getTotalElements();

        return new PageResponseDtoBasic<>(
                pageContent,
                pageNumber,
                pageSize,
                hasNext,
                totalElements
        );
    }
}
