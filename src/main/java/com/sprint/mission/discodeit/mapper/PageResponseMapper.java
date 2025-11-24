package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PageResponseMapper<T> {

public PageResponseDto<T> fromSlice(Slice<T> slice){
    List<T>pageContent = slice.getContent();
    int pageNumber = slice.getNumber();
    int pageSize = slice.getSize();
    boolean hasNext = slice.hasNext();
    Long pageTotalElements = (long) slice.getNumberOfElements();
    return new PageResponseDto<T>(
            pageContent,
            pageNumber,
            pageSize,
            hasNext,
            pageTotalElements
    );
}

public PageResponseDto<T> fromPage(Page<T> page){
    List<T> pageContent = page.getContent();
    int pageNumber = page.getNumber();
    int pageSize = page.getSize();
    boolean hasNext = page.hasNext();
    Long totalElements = page.getTotalElements();

    return new PageResponseDto<T>(
            pageContent,
            pageNumber,
            pageSize,
            hasNext,
            totalElements
    );
}
}
