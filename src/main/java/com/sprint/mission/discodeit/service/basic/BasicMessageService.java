package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * BasicMessageService
 * -----------------
 * MessageService 인터페이스의 구현체로,
 * Repository를 주입받아 실제 메시지 처리 로직을 수행합니다.
 *
 * - 비즈니스 로직: 메시지 생성, 필터링, 최신 메시지 조회 등
 * - 데이터 저장/조회: MessageRepository에 위임
 */
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final MessageMapper messageMapper;

    private final BinaryContentStorage binaryContentStorage;

    /**
     * 새로운 메시지를 생성하여 Repository에 저장
     */
    @Override
    @Transactional
    public MessageResponseDto create(CreateMessageRequestDto request, List<CreateBinaryContentRequestDto> binaryContentRequests) {

        User user = userRepository.findById(request.authorId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 비공개 채널의 경우 유저가 채널에 속해 있는지 검증
        if (channel.getChannelType() == ChannelType.PRIVATE &&
                !readStatusRepository.existsByUserIdAndChannelId(request.authorId(), request.channelId())) {
            throw new CustomException(ErrorCode.NOT_CHANNEL_MEMBER);
        }

        List<BinaryContent> attachments = new ArrayList<>();

        // 메시지에 첨부된 파일이 있는 경우에만 파일 저장
        Optional.ofNullable(binaryContentRequests).ifPresent(
                files -> files.forEach(file -> {
                    BinaryContent fileBytes = new BinaryContent(
                            file.fileName(),
                            file.size(),
                            file.contentType()
                    );
                    attachments.add(fileBytes);
                    binaryContentRepository.save(fileBytes);
                    binaryContentStorage.put(fileBytes.getId(), file.bytes());
                })
        );

        Message newMessage = new Message(
                request.content(),
                channel,
                user,
                attachments
        );

        messageRepository.save(newMessage);

        return messageMapper.toResponseDto(newMessage);
    }

    @Override
    public MessageResponseDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId).
                orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        return messageMapper.toResponseDto(message);
    }

    /**
     * 특정 채널에 포함된 모든 메시지 조회
     */
    @Override
    public Page<MessageResponseDto> findAllByChannelId(UUID channelId, Pageable pageable) {
        return messageRepository.findAllByChannelId(channelId, pageable)
                .map(message -> messageMapper.toResponseDto(message));
    }

    /**
     * 메시지 내용(content) 수정
     */
    @Override
    @Transactional
    public MessageResponseDto update(UUID messageId, UpdateMessageRequestDto request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        message.update(request.newContent());
        messageRepository.save(message);
        return messageMapper.toResponseDto(message);
    }

    /**
     * 특정 메시지(UUID 기반) 삭제
     */
    @Override
    @Transactional
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        List<UUID> attachmentIds = message.getAttachments().stream()
                .map(attachment -> attachment.getId())
                .toList();

        // 메시지에 첨부 파일도 함께 삭제
        binaryContentRepository.deleteByIdIn(attachmentIds);
        messageRepository.deleteById(messageId);
    }
}
