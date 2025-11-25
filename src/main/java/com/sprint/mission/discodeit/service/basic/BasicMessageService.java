package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public Message createMessage(CreateMessageRequestDto request,
      List<CreateBinaryContentRequestDto> fileRequests) {
    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    List<BinaryContent> BinaryContents = new ArrayList<>();

    if (fileRequests != null) {
      for (CreateBinaryContentRequestDto fileRequest : fileRequests) {
        BinaryContent binaryContent = new BinaryContent(
            fileRequest.fileName(),
            fileRequest.contentType(),
            fileRequest.bytes()
        );
        BinaryContents.add(binaryContent);
      }
    }

    Message message = new Message(
        request.content(),
        channel,
        author,
        BinaryContents
    );

    return messageRepository.save(message);
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new IllegalArgumentException("채널이 존재하지 않습니다.");
    }

    return messageRepository.findByChannelId(channelId);
  }

  @Override
  @Transactional
  public Message updateMessage(UUID messageId, UpdateMessageDto MessageUpdateRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

    String newContent = MessageUpdateRequest.newContent();

    message.updateContent(newContent);

    return message;
  }

  @Override
  @Transactional
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
    messageRepository.delete(message);
  }
}
