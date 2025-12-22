package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.Response.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

    public <T> PageResponseDto<T> toResponseDto(Slice<T> slice){
        Object nextCursor = null;

        if(!slice.getContent().isEmpty() && slice.getContent().get(0) instanceof MessageResponseDto){
            MessageResponseDto lastMessage = (MessageResponseDto) slice.getContent()
                    .get(slice.getContent().size() - 1);
            nextCursor = lastMessage.createdAt();
        }

        return new PageResponseDto<>(
                slice.getContent(),
                nextCursor,
                slice.getSize(),
                slice.hasNext(),
                null
        );
    }


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
