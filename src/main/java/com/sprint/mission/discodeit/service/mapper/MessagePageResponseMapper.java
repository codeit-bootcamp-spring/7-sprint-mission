package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


@Component
public class MessagePageResponseMapper implements PageResponseMapper<MessageDto>{


    public PageResponse<MessageDto> fromPage(Page<MessageDto> page) {
        PageResponse<MessageDto> response = new PageResponse<>();
        response.setContent(page.getContent());
        response.setNumber(page.getNumber());
        response.setSize(page.getSize());
        response.setHasNext(page.hasNext());
        response.setTotalElements(page.getTotalElements());
        return response;
    }
}
