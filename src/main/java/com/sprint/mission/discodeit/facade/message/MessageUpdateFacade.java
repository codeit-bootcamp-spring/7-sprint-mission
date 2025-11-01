package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentUpdateReq;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
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

    //메세지 수정
    public void updateMessage(UUID messageId, MessageUpdateReq req){
        List<UUID> updatedAttachmentIds = new ArrayList<>();
        List<BinaryContentUpdateReq> attachmentUpdateReqs = req.attachmentReqs();

        //첨부 파일 없을 때
        if(attachmentUpdateReqs.isEmpty()){
            messageService.update(messageId, req.content(), List.of());
            return;
        }

        //첨부 파일 존재
        attachmentUpdateReqs.forEach(binaryContentUpdateReq -> {
            if(binaryContentUpdateReq.binaryContentId() != null){
                //기존에 있던 파일일 때
                if(binaryContentUpdateReq.data() == null){
                    //삭제된 파일
                    binaryContentService.delete(binaryContentUpdateReq.binaryContentId());
                }else{
                    updatedAttachmentIds.add(binaryContentUpdateReq.binaryContentId());
                }
            }else{
                //새로 업로드 된 파일
                BinaryContent newFile = binaryContentService.create(
                    BinaryContentFactory.create(binaryContentUpdateReq)
                );

                //리스트에도 넣어줌.
                updatedAttachmentIds.add(newFile.getId());
            }
        });
        messageService.update(messageId, req.content(), updatedAttachmentIds);
    }
}
