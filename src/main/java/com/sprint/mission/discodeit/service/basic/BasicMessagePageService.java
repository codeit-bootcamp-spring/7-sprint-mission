package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.MessagePageRepository;
import com.sprint.mission.discodeit.service.MessagePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessagePageService<T> implements MessagePageService<T> {


    private final MessagePageRepository messagePageRepository;
    private final PageResponseMapper<Message> pageResponseMapper;

    @Override
    public PageResponseDto<Message> recentMessagePage() {
        int pageNum = 0;
        int pageSize = 50;
        Pageable pageable = PageRequest.of(
                0,pageSize,
                Sort.by("createdAt").descending()
        );
        Page<Message> targetPage = messagePageRepository.findAll(pageable);

        return  pageResponseMapper.fromPage(targetPage);
    }
}
