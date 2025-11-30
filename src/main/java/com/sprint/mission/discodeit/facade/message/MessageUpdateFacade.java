package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.request.MessageUpdateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.facade.mapper.MessageFacadeMapper;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
  private final BinaryContentStorage binaryContentStorage;

  public MessageViewRes updateMessage(@NonNull UUID messageId, @NonNull MessageUpdateReq req) {
    Message message = messageService.findById(messageId);
    // 기존 첨부파일 중 삭제할 파일 처리
    message.getAttachments().stream()
        .filter(att -> !req.keepAttachmentIds().contains(att.getId()))
        .forEach(att -> {
          binaryContentStorage.delete(att.getId()); //파일 삭제
          binaryContentService.delete(att.getId()); //DB삭제
        });
    message.getAttachments().removeIf(att ->
        !req.keepAttachmentIds().contains(att.getId())); //컬렉션에서 삭제

    // 새로운 첨부파일 생성
    List<BinaryContent> newAttachments = req.newAttachmentReqs().stream()
        .map(r -> {
          BinaryContent binaryContent = binaryContentService.create(BinaryContentFactory.create(r));
          binaryContentStorage.put(binaryContent.getId(), r.data());
          return binaryContent;
        }).toList();

    message.getAttachments().addAll(newAttachments);

    // 메시지 내용 업데이트
    messageService.update(messageId, req.content(), message.getAttachments());
    return messageFacadeMapper.mapToView(message);
  }
}

