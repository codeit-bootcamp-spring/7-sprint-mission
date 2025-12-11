package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;
  private final BinaryContentStorage storage;

  @Override
  @Transactional
  public MessageResponseDto createMessage(CreateMessageRequestDto request,
      List<MultipartFile> fileRequests) throws IOException {
    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    List<BinaryContent> binaryContents = new ArrayList<>();

    if (fileRequests != null) {
      for (MultipartFile fileRequest : fileRequests) {
        BinaryContent binaryContent = new BinaryContent(
            fileRequest.getOriginalFilename(),
            fileRequest.getSize(),
            fileRequest.getContentType()
        );
        BinaryContent saved = binaryContentRepository.save(binaryContent);
        storage.put(saved.getId(), fileRequest.getBytes());
        binaryContents.add(saved);
      }
    }

    Message message = new Message(
        request.content(),
        channel,
        author,
        binaryContents
    );

    Message saved = messageRepository.save(message);
    return messageMapper.toDto(saved);
  }

  @Override
  public PageResponse<MessageResponseDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    if (!channelRepository.existsById(channelId)) {
      throw new IllegalArgumentException("채널이 존재하지 않습니다.");
    }

    Slice<MessageResponseDto> slice = messageRepository.findByChannelId(channelId, pageable)
        .map(messageMapper::toDto);

    return pageResponseMapper.fromSlice(slice);
  }

  @Override
  @Transactional
  public MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto MessageUpdateRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

    String newContent = MessageUpdateRequest.newContent();

    message.updateContent(newContent);

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
    messageRepository.delete(message);
  }
}
