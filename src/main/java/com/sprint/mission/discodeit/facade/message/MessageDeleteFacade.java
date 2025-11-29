package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageDeleteFacade {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;

  //메세지 삭제
  @Transactional
  public void deleteMessage(@NonNull UUID messageId) {
    Message message = messageService.findById(messageId);
    message.getAttachments().forEach(
        attachment -> binaryContentService.delete(attachment.getId()));
    messageService.delete(messageId);
  }
}
