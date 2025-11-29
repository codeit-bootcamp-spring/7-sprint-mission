package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
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
public class MessageUpdateFacade {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;
  private final MessageMapper messageMapper;

  @Transactional
  public MessageViewRes updateMessage(@NonNull UUID messageId, @NonNull MessageUpdateReq req) {
    List<BinaryContent> oldAttachments = messageService.findById(messageId).getAttachments();
    List<BinaryContent> updateAttachments = new ArrayList<>(
        req.keepAttachmentIds().stream().map(binaryContentService::findById).toList()
    );

    //새로운 첨부파일 파일 생성 : 파일 생성 및 updateIds 에 id 넣기
    req.newAttachmentReqs().forEach(r ->
        updateAttachments.add(binaryContentService.create(BinaryContentFactory.create(r))));

    //기존 파일들 중 keep 배열에 없으면 삭제.
    oldAttachments.stream()
        .filter(binaryContent -> !req.keepAttachmentIds().contains(binaryContent.getId()))
        .forEach(binaryContent -> binaryContentService.delete(binaryContent.getId()));

    messageService.update(messageId, req.content(), updateAttachments);
    return messageMapper.mapToView(messageService.findById(messageId));
  }
}

