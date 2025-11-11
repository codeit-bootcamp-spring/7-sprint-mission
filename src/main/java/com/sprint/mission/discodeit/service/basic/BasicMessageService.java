package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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

    private List<UUID> saveAttachment(List<AttachmentDto> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }

        List<UUID> ids = new ArrayList<>();

        for (AttachmentDto attachment : attachments) {
            BinaryContent file = new BinaryContent(
                    attachment.file(),
                    attachment.fileName(),
                    attachment.fileType()
            );
            binaryContentRepository.save(file);

            ids.add(file.getId());
        }
        return ids;
    }

    // Message Create
    @Override
    public MessageResponseDto createDirectMessage(DirectMessageRequestDto requestDto) {
        if ((requestDto.getContent() == null || requestDto.getContent().isBlank()) &&
                (requestDto.getFiles() == null || requestDto.getFiles().isEmpty())) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
        User author = userRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 보내는 사용자를 찾을 수 없음"));
        User receiver = userRepository.findById(requestDto.getReceiverId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 받을 사용자를 찾을 수 없음"));

        List<UUID> attachmentIds = saveAttachment(requestDto.getFiles());

        Message message = new Message(author, receiver, requestDto.getContent(), attachmentIds);
        messageRepository.save(message);

        return MessageResponseDto.from(message);

    }

    @Override
    public MessageResponseDto createChannelMessage(ChannelMessageRequestDto requestDto) {
        if ((requestDto.getContent() == null || requestDto.getContent().isBlank()) &&
                (requestDto.getFiles() == null || requestDto.getFiles().isEmpty())) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }

        User author = userRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 보내는 사용자를 찾을 수 없음"));
        Channel channel = channelRepository.findById(requestDto.getChannelId())
                .orElseThrow(() -> new NotFoundChannelException("메시지를 받을 채널을 찾을 수 없음"));

        // 텍스트만 보내면 getFiles == null
        if (requestDto.getFiles() != null && requestDto.getFiles().size() > 10) {
            throw new InvalidInputException("파일은 한번에 10개까지만 보낼 수 있습니다."); // 예외 임시
        }

        List<UUID> attachmentIds = saveAttachment(requestDto.getFiles());
        Message message = new Message(author, channel, requestDto.getContent(), attachmentIds);
        messageRepository.save(message);

        return MessageResponseDto.from(message);
    }


    // Message Read
    @Override
    public MessageResponseDto findMessageById(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다."));

        return MessageResponseDto.from(message);
    }

    // update를 해도 순서는 바뀌지않음 생성일자로 정렬
    @Override
    public List<MessageResponseDto> findMessageBetweenUsers(UUID userId1, UUID userId2) {
        return messageRepository.findAllByBetweenUserIds(userId1, userId2)
                .stream().sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageResponseDto::from).collect(Collectors.toList());
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageResponseDto::from).collect(Collectors.toList());
    }

    // Message Update
    @Override
    public Optional<MessageResponseDto> updateMessage(MessageUpdateDto updateDto) {
        if (updateDto.newContent() == null || updateDto.newContent().isBlank()) {
            deleteMessage(updateDto.messageId());
            return Optional.empty();
        }

        Message message = messageRepository.findById(updateDto.messageId())
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));

        message.updateContent(updateDto.newContent());
        messageRepository.save(message);
        return Optional.of(MessageResponseDto.from(message));
    }

    // Message Delete
    @Override
    public void deleteMessage(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));

        for (UUID attachmentId : message.getAttachmentIds()){
            binaryContentRepository.deleteById(attachmentId);
        }

        messageRepository.deleteById(id);
    }

    @Override
    public List<MessageResponseDto> findAll() {
        return messageRepository.findAll().stream()
                .map(MessageResponseDto::from).collect(Collectors.toList());
    }
}
