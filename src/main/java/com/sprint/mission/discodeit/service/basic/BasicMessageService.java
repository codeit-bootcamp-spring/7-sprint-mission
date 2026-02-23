package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.page.Response.PageResponseDto;
import com.sprint.mission.discodeit.global.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.global.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.channel.NotChannelMemberException;
import com.sprint.mission.discodeit.global.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

/**
 * BasicMessageService
 * -----------------
 * MessageService 인터페이스의 구현체로,
 * Repository를 주입받아 실제 메시지 처리 로직을 수행합니다.
 *
 * - 비즈니스 로직: 메시지 생성, 필터링, 최신 메시지 조회 등
 * - 데이터 저장/조회: MessageRepository에 위임
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final MessageMapper messageMapper;
    private final PageMapper pageMapper;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 새로운 메시지를 생성하여 Repository에 저장
     */
    @Override
    @Transactional
    public MessageResponseDto create(CreateMessageRequestDto request, List<CreateBinaryContentRequestDto> binaryContentRequests) {

        log.debug("메시지 생성 요청");

        User user = userRepository.findById(request.authorId())
                .orElseThrow(() -> new UserNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", request.authorId())
                ));

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(
                        ErrorCode.CHANNEL_NOT_FOUND,
                        Map.of("channelId", request.channelId())
                ));

        // 비공개 채널의 경우 유저가 채널에 속해 있는지 검증
        if (channel.getChannelType() == ChannelType.PRIVATE &&
                !readStatusRepository.existsByUserIdAndChannelId(request.authorId(), request.channelId())) {
            throw new NotChannelMemberException(
                    ErrorCode.NOT_CHANNEL_MEMBER,
                    Map.of("userId", request.authorId(), "channelId", request.channelId())
            );
        }

        List<BinaryContent> attachments = new ArrayList<>();

        // 메시지에 첨부된 파일이 있는 경우에만 파일 저장
        Optional.ofNullable(binaryContentRequests).ifPresent(
                files -> {
                    log.debug("메시지 첨부파일 저장 시작");
                    files.forEach(file -> {
                            BinaryContent fileBytes = new BinaryContent(
                                    file.fileName(),
                                    file.size(),
                                    file.contentType()
                            );
                            attachments.add(fileBytes);
                        BinaryContent saved = binaryContentRepository.save(fileBytes);

                        eventPublisher.publishEvent(
                                    new BinaryContentCreatedEvent(
                                            saved.getId(),
                                            file.bytes()
                                    )
                            );
                        }
                    );
                    log.debug("메시지 첨부파일 저장 완료");
                }
        );

        Message newMessage = new Message(
                request.content(),
                channel,
                user,
                attachments
        );

        Message saved = messageRepository.save(newMessage);

        eventPublisher.publishEvent(new MessageCreatedEvent(
                saved.getId(),
                channel.getId(),
                user.getId())
        );

        log.info("메시지 생성 완료: messageId = {}", saved.getId());
        return messageMapper.toResponseDto(saved);
    }

    @Override
    public MessageResponseDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId).
                orElseThrow(() -> new MessageNotFoundException(
                        ErrorCode.MESSAGE_NOT_FOUND,
                        Map.of("messageId", messageId)
                ));

        return messageMapper.toResponseDto(message);
    }

    /**
     * 특정 채널에 포함된 모든 메시지 조회
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<MessageResponseDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable) {
        Slice<MessageResponseDto> messageList;

        if(cursor == null) {
            messageList = messageRepository.findAllByChannelId(channelId, pageable)
                    .map(m -> messageMapper.toResponseDto(m));

            return pageMapper.toResponseDto(messageList);
        }

        messageList = messageRepository.findAllByChannelIdAndCreatedAtBefore(channelId, cursor, pageable)
                .map(m -> messageMapper.toResponseDto(m));

        return pageMapper.toResponseDto(messageList);
    }

    /**
     * 메시지 내용(content) 수정
     */
    @Override
    @Transactional
    @PreAuthorize("@messageSecurity.isOwner(#messageId, authentication)")
    public MessageResponseDto update(UUID messageId, UpdateMessageRequestDto request) {

        log.debug("메시지 수정 요청: messageId = {}", messageId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(
                        ErrorCode.MESSAGE_NOT_FOUND,
                        Map.of("messageId", messageId)
                ));

        message.update(request.newContent());
        Message saved = messageRepository.save(message);

        log.info("메시지 수정 완료: messageId = {}", messageId);
        return messageMapper.toResponseDto(saved);
    }

    /**
     * 특정 메시지(UUID 기반) 삭제
     */
    @Override
    @Transactional
    @PreAuthorize("@messageSecurity.isOwner(#messageId, authentication)")
    public void delete(UUID messageId) {

        log.debug("메시지 삭제 요청: messageId = {}", messageId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(
                        ErrorCode.MESSAGE_NOT_FOUND,
                        Map.of("messageId", messageId)
                ));

        List<UUID> attachmentIds = message.getAttachments().stream()
                .map(attachment -> attachment.getId())
                .toList();

        // 메시지에 첨부 파일도 함께 삭제
        binaryContentRepository.deleteByIdIn(attachmentIds);
        messageRepository.deleteById(messageId);

        log.info("메시지 삭제 완료: messageId = {}", messageId);
    }
}
