package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageDeleteFacade {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    //메세지 삭제
    public void deleteMessage(UUID messageId){
        Message message = messageService.findById(messageId);
        message.getAttachmentIds().forEach(binaryContentService::delete);
        messageService.delete(messageId);
    }
}
