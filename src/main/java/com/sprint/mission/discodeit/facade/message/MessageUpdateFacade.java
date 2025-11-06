package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentUpdateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageUpdateFacade {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;
    private final MessageMapper messageMapper;

    public MessageViewRes updateMessage(@NonNull UUID messageId, @NonNull MessageUpdateReq req){
        List<UUID> oldIds = messageService.findById(messageId).getAttachmentIds();
        List<UUID> updatedIds = new ArrayList<>(req.keepAttachmentIds());

        req.newAttachmentReqs().forEach(r ->
                updatedIds.add(binaryContentService.create(BinaryContentFactory.create(r)).getId()));

        oldIds.stream()
                .filter(id -> !req.keepAttachmentIds().contains(id))
                .forEach(binaryContentService::delete);

        messageService.update(messageId, req.content(), updatedIds);
        return messageMapper.mapToView(messageService.findById(messageId));
    }
}

