package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  private final MessageMapper messageMapper;

  @Override
  public MessageResponseDto createMessage(CreateMessageDto createMessageDto,
      List<CreateBinaryContentDto> createBinaryContentDtos) {

    User user = userRepository.findById(createMessageDto.authorId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Channel channel = channelRepository.findById(createMessageDto.channelId())
        .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

    List<UUID> binaryContentIds;
    if (createBinaryContentDtos == null) {
      binaryContentIds = new ArrayList<>();
    } else {
      binaryContentIds = createBinaryContentDtos.stream().map(
          createBinaryContentDto -> {
            BinaryContent binaryContent = new BinaryContent(
                createBinaryContentDto.fileName(),
                createBinaryContentDto.size(),
                createBinaryContentDto.contentType(),
                createBinaryContentDto.bytes());
            binaryContentRepository.save(binaryContent);
            return binaryContent.getId();
          }
      ).toList();
    }

    Message message = new Message(createMessageDto.content(), channel, user);
    messageRepository.save(message);

    return messageMapper.toResponseDto(message);
  }

  @Override
  public MessageResponseDto getMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

    return messageMapper.toResponseDto(message);
  }

  @Override
  public List<MessageResponseDto> getAllMessages() {
    return messageRepository.findAll().stream()
        .map(messageMapper::toResponseDto)
        .toList();
  }

  @Override
  public List<MessageResponseDto> getAllMessageByChannelId(UUID channelID) {
    return messageRepository.findAllByChannelId(channelID).stream()
        .map(messageMapper::toResponseDto)
        .toList();
  }

  @Override
  public MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto updateMessageDto) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));
    message.messageUpdate(updateMessageDto.newContent());
    messageRepository.save(message);

    return messageMapper.toResponseDto(message);
  }

  @Override
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

    messageRepository.deleteById(messageId);
  }

}
