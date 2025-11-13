package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    private List<UUID> saveAttachment(List<MultipartFile> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }

        if (attachments.size() > 10) {
            throw new InvalidInputException("파일은 한번에 10개까지만 보낼 수 있습니다."); // 예외 임시
        }

        List<UUID> ids = new ArrayList<>();

        for (MultipartFile attachment : attachments) {
            try {
                BinaryContent binaryContent = BinaryContent.builder()
                        .binaryData(attachment.getBytes())
                        .dataName(attachment.getOriginalFilename())
                        .dataType(attachment.getContentType())
                        .build();

                binaryContentRepository.save(binaryContent);
                ids.add(binaryContent.getId());

            } catch (IOException e) {
                throw new RuntimeException("오류가 발생", e);
            }
        }
        return ids;
    }

    public Message createMessage(MessageRequestDto requestDto, List<MultipartFile> attachments) {
        if ((requestDto.getContent() == null || requestDto.getContent().isBlank()) &&
                (attachments == null || attachments.isEmpty())) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }

        User author = userRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 보내는 사용자를 찾을 수 없음"));
        Channel channel = channelRepository.findById(requestDto.getChannelId())
                .orElseThrow(() -> new NotFoundChannelException("메시지를 받을 채널을 찾을 수 없음"));

        List<UUID> attachmentIds = saveAttachment(attachments);

        Message message = Message.builder()
                .authorId(requestDto.getAuthorId())
                .channelId(requestDto.getChannelId())
                .content(requestDto.getContent())
                .attachmentIds(attachmentIds)
                .build();
        messageRepository.save(message);

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .collect(Collectors.toList());
    }

    // Message Update
    @Override
    public Message updateMessage(UUID messageId, MessageUpdateDto updateDto) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));

        if (updateDto.newContent() == null || updateDto.newContent().isBlank()) {
            throw new InvalidInputException("비어 있을 수 없습니다.");
        }

        message.updateContent(updateDto.newContent());
        messageRepository.save(message);
        return message;
    }

    // Message Delete
    @Override
    public void deleteMessage(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));

        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.deleteById(attachmentId);
            }
        }

        messageRepository.deleteById(id);
    }
}
