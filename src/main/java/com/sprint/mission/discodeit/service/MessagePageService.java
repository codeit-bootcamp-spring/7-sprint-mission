package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;

public interface MessagePageService<T> {
    PageResponseDto<Message> recentMessagePage();
}
