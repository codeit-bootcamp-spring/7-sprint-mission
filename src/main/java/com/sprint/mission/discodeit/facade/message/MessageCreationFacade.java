package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageCreationFacade {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    //메세지 추가
    public MessageService createMessage(MessageCreateReq req){
        Message message = messageService.create(req);
        List<UUID> attachments = req.attachmentIds();
        if(!attachments.isEmpty()){
            attachments.stream().map(binaryId -> binaryContentService.create(binaryId))
        }
    }
}
