package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.facade.mapper.MessageFacadeMapper;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageUpdateFacade {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;
  private final MessageFacadeMapper messageFacadeMapper;

  public MessageViewRes updateMessage(@NonNull UUID messageId, @NonNull MessageUpdateReq req) {
    Message message = messageService.findById(messageId);
    List<BinaryContent> updateAttachments = new ArrayList<>(req.keepAttachmentIds().stream()
        .map(binaryContentService::findById).toList());

    //새로운 첨부파일 파일 생성 : 파일 생성 및 updateIds 에 id 넣기
    req.newAttachmentReqs().forEach(r ->
        updateAttachments.add(binaryContentService.create(BinaryContentFactory.create(r))));

    //기존 파일들 중 keep 배열에 없으면 삭제.
    message.getAttachments().removeIf(att -> !req.keepAttachmentIds().contains(att.getId()));
    message.getAttachments().addAll(
        updateAttachments.stream()
            .filter(att -> !message.getAttachments().contains(att)) // 중복 방지
            .toList()
    );

    messageService.update(messageId, req.content(), message.getAttachments());
    return messageFacadeMapper.mapToView(message);
  }
}

