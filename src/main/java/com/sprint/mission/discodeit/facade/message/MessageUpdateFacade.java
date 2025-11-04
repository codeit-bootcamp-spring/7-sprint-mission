package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentUpdateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
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

    //메세지 수정
    public MessageViewRes updateMessage(UUID messageId, MessageUpdateReq req){
        List<UUID> updatedAttachmentIds = new ArrayList<>();
        List<BinaryContentUpdateReq> attachmentUpdateReqs = req.attachmentReqs();

        attachmentUpdateReqs.forEach(binaryContentUpdateReq -> {
            if(binaryContentUpdateReq.binaryContentId() != null){
                handleExistingFile(binaryContentUpdateReq, updatedAttachmentIds);
            }else{
                handleNewFile(binaryContentUpdateReq, updatedAttachmentIds);
            }
        });

        messageService.update(messageId, req.content(), updatedAttachmentIds);
        return messageMapper.mapToView(messageService.findById(messageId));
    }

    //기존 파일(데이터 없으면 삭제, 있으면 유지)
    private void handleExistingFile(BinaryContentUpdateReq req, List<UUID> attachmentIds){
        if(req.data() == null){
            binaryContentService.delete(req.binaryContentId());
        }else{
            attachmentIds.add(req.binaryContentId());
        }
    }

    //새로 업로드 된 파일
    private void handleNewFile(BinaryContentUpdateReq req, List<UUID> attachmentIds){
        BinaryContent newFile = binaryContentService.create(
            BinaryContentFactory.create(req)
        );
        attachmentIds.add(newFile.getId());
    }
}

