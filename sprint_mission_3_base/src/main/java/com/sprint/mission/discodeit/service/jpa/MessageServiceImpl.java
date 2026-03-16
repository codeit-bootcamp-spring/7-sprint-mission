package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final PageResponseMapper pageResponseMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public MessageDto create(
            MessageCreateRequest request,
            List<BinaryContentCreateRequest> binaryRequests
    ) {
        User user = userRepository.findById(request.authorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return createInternal(user, request.channelId(), request.content(), binaryRequests);
    }

    @Override
    public MessageDto createWithAuthorId(UUID authorId, MessageCreateRequest request) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return createInternal(user, request.channelId(), request.content(), List.of());
    }

    private MessageDto createInternal(
            User user,
            UUID channelId,
            String content,
            List<BinaryContentCreateRequest> binaryRequests
    ) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        List<BinaryContent> attachments = new ArrayList<>();
        for (BinaryContentCreateRequest br : binaryRequests) {
            BinaryContent binary = new BinaryContent(
                    br.fileName(),
                    (long) br.bytes().length,
                    br.contentType()
            );
            binaryContentRepository.save(binary);
            attachments.add(binary);
            eventPublisher.publishEvent(new BinaryContentCreatedEvent(binary.getId(), br.bytes()));
        }

        Message message = new Message(
                content,
                channel,
                user,
                attachments
        );

        messageRepository.save(message);

        eventPublisher.publishEvent(new MessageCreatedEvent(
                message.getId(),
                channel.getId(),
                user.getId(),
                message.getContent()
        ));

        return MessageDto.from(messageRepository.findDetailById(message.getId()).orElse(message));
    }

    @Override
    public MessageDto find(UUID messageId) {
        return messageRepository.findDetailById(messageId)
                .map(MessageDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    @Override
    public PageResponse<MessageDto> findAllByChannelId(
            UUID channelId,
            Pageable pageable
    ) {
        var slice = messageRepository.findAllByChannelIdWithAuthor(
                channelId,
                Instant.now(),
                pageable
        );
        Instant nextCursor = null;
        if (!slice.getContent().isEmpty()) {
            nextCursor = slice.getContent()
                    .get(slice.getContent().size() - 1)
                    .getCreatedAt();
        }

        var dtoSlice = slice.map(MessageDto::from);
        return pageResponseMapper.fromSlice(dtoSlice, nextCursor);
    }

    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        message.update(request.newContent());
        return MessageDto.from(messageRepository.findDetailById(message.getId()).orElse(message));
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.deleteById(messageId);
    }
}