package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.facade.mapper.MessageMapper;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.factory.MessageFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
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
public class MessageCreationFacade {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;
  private final MessageMapper messageMapper;
  private final ChannelService channelService;
  private final MessageFactory messageFactory;

  //메세지 추가
  public MessageViewRes createMessage(@NonNull UUID speakerId, @NonNull UUID channelId,
      @NonNull MessageCreateReq req) {
    if (channelService.findById(channelId) == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    List<BinaryContent> attachments = new ArrayList<>();

    if (!req.attachmentIds().isEmpty()) {
      req.attachmentIds().forEach(BinaryContentReq -> {
        BinaryContent newBinaryContent = binaryContentService.create(
            BinaryContentFactory.create(BinaryContentReq)
        );
        attachments.add(newBinaryContent);
      });
    }
    Message message = messageService.create(
        messageFactory.create(speakerId, channelId, req, attachments));
    return messageMapper.mapToView(message);
  }
}

