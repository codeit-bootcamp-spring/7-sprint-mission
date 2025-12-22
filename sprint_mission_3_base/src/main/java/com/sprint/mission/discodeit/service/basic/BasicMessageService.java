package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;

    @Transactional
    @Override
    public MessageDto create(
            MessageCreateRequest messageCreateRequest,
            List<BinaryContentCreateRequest> binaryContentCreateRequests
    ) {
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new NoSuchElementException("Channel with id " + channelId + " does not exist"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() ->
                        new NoSuchElementException("Author with id " + authorId + " does not exist"));

        List<BinaryContent> attachments = binaryContentCreateRequests.stream()
                .map(req -> {
                    BinaryContent binary = new BinaryContent(
                            req.fileName(),
                            (long) req.bytes().length,
                            req.contentType()
                    );
                    binaryContentRepository.save(binary);
                    binaryContentStorage.put(binary.getId(), req.bytes());
                    return binary;
                })
                .toList();

        Message message = new Message(
                messageCreateRequest.content(),
                channel,
                author,
                attachments
        );

        messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto find(UUID messageId) {
        return messageRepository.findById(messageId)
                .map(messageMapper::toDto)
                .orElseThrow(() ->
                        new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    // ✅ 인터페이스 변경에 맞춰 추가
    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
        throw new UnsupportedOperationException("BasicMessageService: not implemented");
    }

    @Transactional
    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new NoSuchElementException("Message with id " + messageId + " not found"));

        message.update(request.newContent());
        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        messageRepository.deleteById(messageId);
    }
}
