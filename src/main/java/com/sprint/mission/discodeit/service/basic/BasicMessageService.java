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
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final BinaryContentStorage binaryContentStorage;
    private final PageResponseMapper pageResponseMapper;


    @Override
    @Transactional
    public MessageResponseDto createMessage(CreateMessageDto createMessageDto,
                                            List<CreateBinaryContentDto> createBinaryContentDtos) {

        User author = userRepository.findById(createMessageDto.authorId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Channel channel = channelRepository.findById(createMessageDto.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

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

                        binaryContentStorage.put(binaryContent.getId(), createBinaryContentDto.bytes());
                        binaryContentRepository.save(binaryContent);

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

        return messageMapper.toResponseDto(message);
    }

    @Override
    @Transactional(readOnly = true)
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
    public MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto updateMessageDto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        message.messageUpdate(updateMessageDto.newContent());
        messageRepository.save(message);

        return messageMapper.toResponseDto(message);
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {
        if (!messageRepository.existsById(messageId))
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);

        messageRepository.deleteById(messageId);
    }

}
