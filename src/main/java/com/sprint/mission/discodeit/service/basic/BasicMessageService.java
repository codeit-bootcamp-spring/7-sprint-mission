package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message createMessage(CreateMessageRequestDto request,
      List<CreateBinaryContentRequestDto> fileRequests) {
    userRepository.findById(request.authorId())
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    channelRepository.findById(request.channelId())
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    List<UUID> attachmentIds = new ArrayList<>();

    if (fileRequests != null) {
      for (CreateBinaryContentRequestDto fileRequest : fileRequests) {
        BinaryContent binaryContent = new BinaryContent(
            fileRequest.data(),
            fileRequest.fileName(),
            fileRequest.fileType()
        );
        BinaryContent saved = binaryContentRepository.save(binaryContent);
        attachmentIds.add(saved.getId());
      }
    }

    Message message = new Message(
        request.content(),
        request.channelId(),
        request.authorId(),
        attachmentIds
    );

    return messageRepository.save(message);
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    return messageRepository.findByChannelId(channelId);
  }

  @Override
  public Message updateMessage(UUID messageId, UpdateMessageDto MessageUpdateRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

    String newContent = MessageUpdateRequest.newContent();

    message.updateContent(newContent);

    return messageRepository.save(message);
  }

  @Override
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

    List<UUID> attachmentIds = message.getAttachmentIds();

    for (UUID attachmentId : attachmentIds) {
      binaryContentRepository.delete(attachmentId);
    }
    messageRepository.delete(messageId);
  }
}
