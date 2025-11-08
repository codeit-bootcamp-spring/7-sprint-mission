package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.factory.MessageFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.transactional.CustomTransactional;
import lombok.NonNull;
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

    //메세지 추가
    @CustomTransactional
    public Message createMessage(@NonNull UUID speakerId, @NonNull UUID channelId, @NonNull MessageCreateReq req){
        List<UUID> attachments = new ArrayList<>();
        
        if(!req.attachmentIds().isEmpty()){
            req.attachmentIds().forEach(BinaryContentReq ->{
                BinaryContent newBinaryContent = binaryContentService.create(
                        BinaryContentFactory.create(BinaryContentReq)
                );
                attachments.add(newBinaryContent.getId());
            });
        }

        return messageService.create(MessageFactory.create(speakerId, channelId, req, attachments));
    }
}

