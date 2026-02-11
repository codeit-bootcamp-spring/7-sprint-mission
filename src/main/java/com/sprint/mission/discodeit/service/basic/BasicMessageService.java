package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageSendCommand;
import com.sprint.mission.discodeit.dto.message.MessageUpdateCommand;
import com.sprint.mission.discodeit.dto.message.MessageUpdateResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageSendNotAllowed;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.MessageReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;
    private final MessageReader messageReader;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;
    private final ReadStatusRepository readStatusRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponseDto> getAllMessages() {
        List<Message> allMessages = messageRepository.findAll();
        return allMessages
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PageResponse<MessageResponseDto> getAllMessagesByChannelId(UUID channelId, Pageable pageable, Instant cursor) {
        if (channelId == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("메시지 목록 조회 시도 - channelId={}, page={}, size={}, cursor={}",
                channelId, pageable.getPageNumber(), pageable.getPageSize(), cursor);

        Channel channel = channelReader.findChannelOrThrow(channelId);

        /**
         NOTE: DTO 변환이유중하나가 엔티티를 그대로 컨트롤러까지 넘기면, @Transactional 범위 밖에서 직렬화(Jackson)가 author 같은 lazy 연관필드에 접근하면서
         영속성 컨텍스트/세션이 이미 닫혀 있는데 lazy 필드는 db에서 안 가져온 상태(프록시)라서 프록시를 초기화 하려고할때 LazyInitializationException 이 발생한다.
         그래서 트랜잭션 안에서 미리 DTO로 변환해서 필요한 값만 꺼내두는 것.
         */

        Slice<MessageResponseDto> sliceMessageList = messageRepository.findAllByChannelId(channel.getId(), pageable, Optional.ofNullable(cursor).orElse(Instant.now()))
                .map(messageMapper::toDto);
        // NOTE: fetch join, batch 이용 N+1, pagination 해결

        Instant nextCursor = null;
        if (!sliceMessageList.getContent().isEmpty()) {
            nextCursor = sliceMessageList.getContent().get(sliceMessageList.getContent().size() - 1) // NOTE: 이부분에 대해서 좀더 볼것
                    .createdAt();
        }

        log.debug("메시지 목록 조회 성공 - channelId={}, count={}, nextCursor={}",
                channelId, sliceMessageList.getNumberOfElements(), nextCursor);

        return PageResponseMapper.fromSlice(sliceMessageList, nextCursor);

    }

    @Override
    @Transactional(readOnly = true)
    public MessageResponseDto getMessageById(UUID messageId) {
        Message message = messageReader.findMessageOrThrow(messageId);
        return messageMapper.toDto(message);
    }


    @Override
    @Transactional
    public MessageResponseDto sendMessageToChannel(MessageSendCommand command) {
        if (command.content() == null) { // NOTE: 서비스 레이어 public API라 컨트롤러 외 테스트, 배치, 이벤트 핸들러에서 요청 가능하므로 최소 필수 가드로 남김
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("메시지 전송 시도 - channelId={}, senderId={}, attachments={}",
                command.channelId(), command.senderId(),
                command.profiles() == null ? 0 : command.profiles().size());

        // NOTE: 1. 보내려는 유저가 맞는지 확인
        User sender = userReader.findUserOrThrow(command.senderId());
        // NOTE: 2. 보내려는 채널이있는지 확인
        Channel channel = channelReader.findChannelOrThrow(command.channelId());

        // 3. PRIVATE 채널일 때만 멤버 체크
        if (channel.getType() == ChannelType.PRIVATE) {
            boolean isMember = readStatusRepository.existsByUserIdAndChannelId(sender.getId(), channel.getId());
            if (!isMember) {
                throw new MessageSendNotAllowed(sender.getId(), channel.getId(), channel.getType());
            }
        }

        List<UUID> profileBinaryIds = command.profiles().stream()
                .map(binaryContentService::uploadBinaryContent).toList();

        // UUID → BinaryContent 조회
        List<BinaryContent> attachments = binaryContentRepository.findAllById(profileBinaryIds);
        Message message = Message.builder()
                .content(command.content())
                .author(sender)
                .channel(channel)
                .attachments(attachments)
                .build();

        Message savedMessage = messageRepository.save(message);

        log.info("메시지 전송 성공 - messageId={}, channelId={}, senderId={}",
                savedMessage.getId(), channel.getId(), sender.getId());

        return messageMapper.toDto(savedMessage);
    }

    @Override
    @Transactional
    @PreAuthorize("principal.userResponseDto.id() == @basicMessageService.getMessageById(#command.messageId()).author().id()")
    public MessageUpdateResponseDto updateMessage(MessageUpdateCommand command) {
        if (command.messageId() == null || command.content() == null || command.content().trim().isEmpty()) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }
        log.debug("메시지 수정 시도 - messageId={}", command.messageId());
        Message message = messageReader.findMessageOrThrow(command.messageId());


        boolean isUpdated = message.updateContent(command.content());
        if (isUpdated) {
            log.info("메시지 수정 완료 - messageId={}", message.getId());
        } else {
            log.debug(
                    "메시지 수정 없음 - messageId={}, reason=same_content",
                    command.messageId()
            );
        }
        return messageMapper.toUpdateDto(message);
    }

    @Override
    @Transactional
    @PreAuthorize("principal.userResponseDto.id() == @basicMessageService.getMessageById(#messageId).author().id()")
    public void deleteMessage(UUID messageId) {
        if (messageId == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("메시지 삭제 시도 - messageId={}", messageId);

        Message message = messageReader.findMessageOrThrow(messageId);
        messageRepository.deleteById(message.getId());

        log.info("메시지 삭제 완료 - messageId={}", messageId);
    }
}
