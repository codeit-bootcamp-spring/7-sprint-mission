package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public MessageResponseDto createMessage(CreateMessageDto createMessageDto,
            List<CreateBinaryContentDto> createBinaryContentDtos) {

        User author = userRepository.findById(createMessageDto.authorId())
                .orElseThrow(() -> UserNotFoundException.byId(createMessageDto.authorId()));

        Channel channel = channelRepository.findById(createMessageDto.channelId())
                .orElseThrow(() -> ChannelNotFoundException.byId(createMessageDto.channelId()));

        List<BinaryContent> attachments;
        if (createBinaryContentDtos == null) {
            attachments = new ArrayList<>();
        } else {
            attachments = createBinaryContentDtos.stream().map(
                    createBinaryContentDto -> {
                        BinaryContent binaryContent = BinaryContent.builder()
                                .fileName(createBinaryContentDto.fileName())
                                .size(createBinaryContentDto.size())
                                .contentType(createBinaryContentDto.contentType())
                                .build();
                        binaryContentRepository.save(binaryContent);
                        eventPublisher.publishEvent(new BinaryContentCreatedEvent(this, binaryContent.getId(), createBinaryContentDto.bytes()));

                        return binaryContent;
                    }
            ).toList();
        }

        Message message = Message.builder()
                .content(createMessageDto.content())
                .channel(channel)
                .author(author)
                .attachments(attachments)
                .build();

        messageRepository.save(message);
        MessageResponseDto messageResponseDto = messageMapper.toResponseDto(message);
        eventPublisher.publishEvent(new MessageCreatedEvent(this, messageResponseDto, channel.getName()));
        return messageResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public MessageResponseDto getMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));

        return messageMapper.toResponseDto(message);
    }

    @Override
    public List<MessageResponseDto> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(messageMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageResponseDto> getAllMessageByChannelId(UUID channelID, Instant createdAt, Pageable pageable) {
        Instant cursor = (createdAt == null) ? Instant.now() : createdAt;
        Slice<MessageResponseDto> slice = messageRepository
                .findAllByChannelId(channelID, cursor, pageable)
                .map(messageMapper::toResponseDto);

        Instant nextCursor = null;
        if (!slice.getContent().isEmpty()) {
            nextCursor = slice.getContent().get(slice.getContent().size() - 1).createdAt();
        }

        return pageResponseMapper.toSliceResponseDto(slice, nextCursor);

    }

    @Override
    @Transactional
    @PreAuthorize("@messageSecurity.isOwner(#messageId, authentication)")
    public MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto updateMessageDto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));

        message.messageUpdate(updateMessageDto.newContent());

        return messageMapper.toResponseDto(message);
    }

    @Override
    @Transactional
    @PreAuthorize("@messageSecurity.isOwner(#messageId, authentication)")
    public void deleteMessage(UUID messageId) {
        if (!messageRepository.existsById(messageId))
            throw MessageNotFoundException.byId(messageId);

        messageRepository.deleteById(messageId);
    }

}
