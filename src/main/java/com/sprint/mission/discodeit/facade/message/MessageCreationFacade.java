package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.factory.MessageFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageCreationFacade {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;
    private final MessageMapper messageMapper;

    //메세지 추가
    public MessageViewRes createMessage(MessageCreateReq req){
        List<UUID> attachments = new ArrayList<>();
        
        if(!req.attachmentIds().isEmpty()){
            req.attachmentIds().forEach(BinaryContentReq ->{
                BinaryContent newBinaryContent = binaryContentService.create(
                        BinaryContentFactory.create(BinaryContentReq)
                );
                attachments.add(newBinaryContent.getId());
            });
        }

        Message newMessage = messageService.create(MessageFactory.create(req, attachments));
        return messageMapper.mapToView(newMessage);
    }
}

