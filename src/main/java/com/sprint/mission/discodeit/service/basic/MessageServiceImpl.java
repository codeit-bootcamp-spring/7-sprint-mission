package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.dto.messageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binaryContent.FileOperationFailedException;
import com.sprint.mission.discodeit.exception.binaryContent.FileUploadLimitExceedException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotEmptyException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentStorage binaryContentStorage;

    private List<BinaryContent> saveAttachment(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }
        if (files.size() > 10) {
            throw new FileUploadLimitExceedException();
        }

        List<BinaryContent> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            BinaryContent binaryContent = BinaryContent.builder()
                    .fileName(file.getOriginalFilename())
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .build();

            binaryContentRepository.save(binaryContent);
            attachments.add(binaryContent);
            try {
                binaryContentStorage.put(binaryContent.getId(), file.getBytes());
            } catch (IOException e) {
                throw new FileOperationFailedException(binaryContent.getId());
            }
        }
        return attachments;
    }

    @Override
    @Transactional
    public MessageDto createMessage(MessageCreateRequest requestDto, List<MultipartFile> files) {
        if ((requestDto.getContent() == null || requestDto.getContent().isBlank()) &&
                (files == null || files.isEmpty())) {
            throw new MessageNotEmptyException();
        }

        User author = userRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(requestDto.getAuthorId()));
        Channel channel = channelRepository.findById(requestDto.getChannelId())
                .orElseThrow(() -> new ChannelNotFoundException(requestDto.getChannelId()));

        List<BinaryContent> attachments = saveAttachment(files);

        Message message = Message.builder()
                .author(author)
                .channel(channel)
                .content(requestDto.getContent())
                .attachments(attachments)
                .build();
        messageRepository.save(message);

        return messageMapper.toDto(message);
    }

    // 오프셋 기반 페이징
//    @Override
//    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
//
//        Slice<Message> messagesSlice = messageRepository.findAllByChannelId(channelId, pageable);
//
//        Slice<MessageDto> dto = messagesSlice.map(messageMapper::toDto);
//
//        return PageResponse.from(dto);
//    }

    // 커서 기반 페이징
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable) {

        Instant cursorAt = (cursor == null) ? Instant.now() : cursor;

        int size = pageable.getPageSize();  // size = 50

        Pageable limit = PageRequest.of(0, size + 1);   // limit = 51
        List<Message> messages = messageRepository.findAllByChannelId(channelId, cursorAt, limit);  // 메시지 51개

        Object nextCursor = null;
        boolean hasNext = false;

        if (messages.size() > size) {
            messages.remove(size);  // message.remove(50) -> 51번째 메시지 삭제
            nextCursor = messages.get(messages.size() - 1).getCreatedAt();  // message.get(49) -> 50번째 메시지
            hasNext = true;
        }

        List<MessageDto> dto = messages.stream().map(messageMapper::toDto).collect(Collectors.toList());

        // List<T> content, Object nextCursor, int size, boolean hasNext, Long totalElements
        return PageResponse.from(dto, nextCursor, size, hasNext, null);
    }

    // Message Update
    @Override
    @Transactional
    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest updateDto) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        if (updateDto.newContent() == null || updateDto.newContent().isBlank()) {
            throw new MessageNotEmptyException(messageId);
        }

        message.updateContent(updateDto.newContent());
        messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    // Message Delete
    @Override
    @Transactional
    public void deleteMessage(UUID id) {
        messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));

        messageRepository.deleteById(id);
    }
}
