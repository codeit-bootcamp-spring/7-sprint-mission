package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.entity.binaryContent.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.dto.messageDto.*;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    private MessageInfoDto toDto(Message message) {
        List<BinaryContent> attachments = binaryContentRepository.findAllByMessageId(message.getId());
        return MessageInfoDto.from(message, attachments);
    }

    private void SaveAttachment(Message message, List<AttachmentDto> attachments) {
        if (attachments != null && !attachments.isEmpty()) {
            for (AttachmentDto attachment : attachments) {
                BinaryContent file = new BinaryContent(
                        message.getAuthor().getId(),
                        message.getId(),
                        attachment.file(),
                        attachment.fileName(),
                        attachment.fileType()
                );
                binaryContentRepository.save(file);
            }
        }
    }


    // Message Create
    @Override
    public MessageInfoDto createDirectMessage(DirectMessageCreateRequestDto createDto) {
        if ((createDto.getContent() == null || createDto.getContent().isBlank()) &&
                (createDto.getFiles() == null || createDto.getFiles().isEmpty())) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
        User author = userRepository.findById(createDto.getAuthorId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 보내는 사용자를 찾을 수 없음"));
        User receiver = userRepository.findById(createDto.getReceiverId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 받을 사용자를 찾을 수 없음"));
        Message message = new Message(author, receiver, createDto.getContent());
        messageRepository.save(message);

        SaveAttachment(message, createDto.getFiles());
        return toDto(message);

    }

    @Override
    public MessageInfoDto createChannelMessage(ChannelMessageCreateRequestDto createDto) {
        if ((createDto.getContent() == null || createDto.getContent().isBlank()) &&
                createDto.getFiles() == null || createDto.getFiles().isEmpty()) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }

        User author = userRepository.findById(createDto.getAuthorId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 보내는 사용자를 찾을 수 없음"));
        Channel channel = channelRepository.findById(createDto.getChannelId())
                .orElseThrow(() -> new NotFoundChannelException("메시지를 받을 채널을 찾을 수 없음"));

        // 텍스트만 보내면 getFiles == null
        if (createDto.getFiles() != null && createDto.getFiles().size() > 10) {
            throw new InvalidInputException("파일은 한번에 10개까지만 보낼 수 있습니다."); // 예외 임시
        }

        Message message = new Message(author, channel, createDto.getContent());
        messageRepository.save(message);
        SaveAttachment(message, createDto.getFiles());
        return toDto(message);
    }


    // Message Read
    @Override
    public Optional<MessageInfoDto> findMessageById(UUID messageId) {
        return messageRepository.findById(messageId).map(this::toDto);
    }

    // update를 해도 순서는 바뀌지않음 생성일자로 정렬
    @Override
    public List<MessageInfoDto> findMessageBetweenUsers(UUID userId1, UUID userId2) {
        return messageRepository.findAllByBetweenUserIds(userId1, userId2)
                .stream().sorted(Comparator.comparing(Message::getCreatedAt))
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MessageInfoDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(this::toDto).collect(Collectors.toList());
    }

    // Message Update
    @Override
    public Optional<MessageInfoDto> updateMessage(MessageUpdateDto updateDto) {
        if (updateDto.newContent() == null || updateDto.newContent().isBlank()) {
            deleteMessage(updateDto.messageId());
            return Optional.empty();
        }

        return messageRepository.findById(updateDto.messageId()).map(message -> {
            message.updateContent(updateDto.newContent());
            messageRepository.save(message);
            return toDto(message);
        });
    }

    // Message Delete
    @Override
    public boolean deleteMessage(UUID id) {

        return messageRepository.findById(id).map(message -> {
            binaryContentRepository.deleteAllByMessageId(message.getId());
            messageRepository.deleteById(id);
            return true;
        }).orElse(false);

    }
}
