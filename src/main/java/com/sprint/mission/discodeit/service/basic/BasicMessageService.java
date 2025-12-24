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
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    log.debug("메시지 생성 시작 - 채널 id : {}", request.channelId());
    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> {
          log.warn("메시지 생성 실패 - 존재하지 않는 유저: {}", request.authorId());
          return new UserNotFoundException(request.authorId());
        });
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> {
          log.warn("메시지 생성 실패 - 존재하지 않는 채널: {}", request.channelId());
          return new ChannelNotFoundException(request.channelId());
        });

    List<BinaryContent> binaryContents = new ArrayList<>();

    if (fileRequests != null) {
      log.debug("첨부파일 {}개 업로드 시작", fileRequests.size());
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
      log.debug("첨부파일 업로드 완료 - 첨부파일 {}개", binaryContents.size());
    }

    Message message = new Message(
        request.content(),
        channel,
        author,
        binaryContents
    );

    Message saved = messageRepository.save(message);
    log.info("메시지 생성 완료 - 메시지 ID: {}, 채널명: {} 첨부파일: {}개",
        saved.getId(), saved.getChannel().getName(), binaryContents.size());
    return messageMapper.toDto(saved);
  }

  @Override
  public PageResponse<MessageResponseDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException(channelId);
    }

    Slice<MessageResponseDto> slice = messageRepository.findByChannelId(channelId, pageable)
        .map(messageMapper::toDto);

    return pageResponseMapper.fromSlice(slice);
  }

  @Override
  @Transactional
  public MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto MessageUpdateRequest) {
    log.debug("메시지 수정 시작 - 메시지 id: {}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("메시지 수정 실패 - 존재하지 않는 메시지 id: {}", messageId);
          return new MessageNotFoundException(messageId);
        });

    String newContent = MessageUpdateRequest.newContent();

    message.updateContent(newContent);
    log.info("메시지 수정 완료 - 메시지 id: {}", messageId);
    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void deleteMessage(UUID messageId) {
    log.debug("메시지 삭제 시작 - 메시지 id: {}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("메시지 삭제 실패 - 존재하지 않는 메시지 id: {}", messageId);
          return new MessageNotFoundException(messageId);
        });
    log.info("메시지 삭제 성공 - 메시지 id: {}", messageId);
    messageRepository.delete(message);
  }
}
