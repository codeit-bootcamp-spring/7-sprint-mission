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
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage storage;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public MessageDto create(
            MessageCreateRequest request,
            List<BinaryContentCreateRequest> binaryRequests
    ) {
        User user = userRepository.findById(request.authorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        Message message = new Message(
                request.content(),
                channel,
                user,
                List.of()
        );

        messageRepository.save(message);

        for (BinaryContentCreateRequest br : binaryRequests) {
            BinaryContent binary = new BinaryContent(
                    br.fileName(),
                    (long) br.bytes().length,
                    br.contentType()
            );
            binaryContentRepository.save(binary);
            storage.put(binary.getId(), br.bytes());
        }

        return MessageDto.from(message);
    }

    @Override
    public MessageDto find(UUID messageId) {
        return messageRepository.findById(messageId)
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
        return MessageDto.from(message);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.deleteById(messageId);
    }

}
