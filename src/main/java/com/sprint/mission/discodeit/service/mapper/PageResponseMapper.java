package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponse;
import org.springframework.data.domain.Page;

public interface PageResponseMapper<T> {
    PageResponse<T> fromPage(Page<T> page);
}
