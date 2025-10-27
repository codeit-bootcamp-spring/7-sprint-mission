package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.messageDto.ChannelMessageCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.DirectMessageCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageInfoDto;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepo;
    private final UserService userService;
    private final ChannelService channelService;

    // Message Create
    @Override
    public MessageInfoDto createDirectMessage(DirectMessageCreateRequestDto createDto) {
        if (createDto.getContent() == null || createDto.getContent().isBlank()) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
        User author = userService.findUserEntityById(createDto.getAuthorId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 보내는 사용자를 찾을 수 없음"));
        User receiver = userService.findUserEntityById(createDto.getReceiverId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 받을 사용자를 찾을 수 없음"));
        Message message = new Message(author, receiver, createDto.getContent());
        messageRepo.save(message);
        return MessageInfoDto.from(message);

    }

    @Override
    public MessageInfoDto createChannelMessage(ChannelMessageCreateRequestDto createDto) {
        if (createDto.getContent() == null || createDto.getContent().isBlank()) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
        User author = userService.findUserEntityById(createDto.getAuthorId())
                .orElseThrow(() -> new NotFoundUserException("메시지를 보내는 사용자를 찾을 수 없음"));
        Channel channel = channelService.findChannelEntityById(createDto.getChannelId())
                .orElseThrow(() -> new NotFoundChannelException("메시지를 받을 채널을 찾을 수 없음"));
        Message message = new Message(author, channel, createDto.getContent());
        messageRepo.save(message);
        return MessageInfoDto.from(message);

    }


    // Message Read
    @Override
    public Optional<MessageInfoDto> findMessageById(UUID messageId) {
        return messageRepo.findById(messageId).map(MessageInfoDto::from);
    }

    // update를 해도 순서는 바뀌지않음 생성일자로 정렬
    @Override
    public List<MessageInfoDto> findMessageBetweenUsers(UUID userId1, UUID userId2) {
        return messageRepo.findAllByBetweenUserIds(userId1, userId2)
                .stream().sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfoDto::from).collect(Collectors.toList());
    }

    @Override
    public List<MessageInfoDto> findChannelMessage(UUID channelId) {
        return messageRepo.findAllByChannelId(channelId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfoDto::from).collect(Collectors.toList());
    }

    // Message Update
    @Override
    public Optional<MessageInfoDto> updateMessage(UUID messageId, String newContent) {
        if (newContent == null || newContent.isBlank()) {
            deleteMessage(messageId);
            return Optional.empty();
        }

        return messageRepo.findById(messageId).map(message -> {
            message.updateContent(newContent);
            messageRepo.save(message);
            return MessageInfoDto.from(message);
        });
    }

    // Message Delete
    @Override
    public boolean deleteMessage(UUID id) {

        return messageRepo.findById(id).map(message -> {
            messageRepo.deleteById(id);
            return true;
        }).orElse(false);

        /*
        if (messageRepo.findById(id).isPresent()) {
            messageRepo.deleteById(id);
            return true;
        }
        return false;
         */
    }
}
