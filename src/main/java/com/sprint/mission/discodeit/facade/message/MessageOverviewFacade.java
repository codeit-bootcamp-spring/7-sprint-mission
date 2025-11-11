package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageOverviewFacade {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    //메세지 전체 조회
    public List<MessageViewRes> findAllByChannelId(@NonNull UUID channelId){
        return messageService.findAllByChannelId(channelId).stream()
                .map(messageMapper::mapToView).toList();
    }
}
