package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageOverviewFacade {
    private final MessageService messageService;
    private final UserService userService;
    private final BinaryContentService binaryContentService;

    //메세지 전체 조회
    public List<MessageViewRes> findAllByChannelId(UUID channelId){
        return messageService.findAllByChannelId(channelId).stream()
                .map(this::mapToView).toList();
    }
    
    //변환 메소드
    private MessageViewRes mapToView(Message message){
        List<BinaryContentInfoRes> imgs = message.getAttachmentIds().stream()
                .map(binaryId -> BinaryContentInfoRes.from(
                        binaryContentService.findById(binaryId))
                ).toList();

        return MessageViewRes.from(
                message,
                userService.findById(message.getSpeakerId()).getNickname(),
                imgs
        );
    }
}
