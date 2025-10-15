package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.*;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.*;

import java.util.*;
import java.util.stream.Collectors;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepo;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepo, UserService userService, ChannelService channelService) {
        this.messageRepo = messageRepo;
        this.userService = userService;
        this.channelService = channelService;
    }

    // Message Create
    @Override
    public MessageInfo createDirectMessage(UUID authorId, UUID receiverId, String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
        User author = userService.findUserEntityById(authorId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 보내는 사용자를 찾을 수 없음"));
        User receiver = userService.findUserEntityById(receiverId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 받을 사용자를 찾을 수 없음"));
        Message message = new Message(author, receiver, content);
        messageRepo.save(message);
        return new MessageInfo(message);

    }

    @Override
    public MessageInfo createChannelMessage(UUID authorId, UUID channelId, String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
        User author = userService.findUserEntityById(authorId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 보내는 사용자를 찾을 수 없음"));
        Channel channel = channelService.findChannelEntityById(channelId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 받을 채널을 찾을 수 없음"));
        Message message = new Message(author, channel, content);
        messageRepo.save(message);
        return new MessageInfo(message);

    }


    // Message Read
    @Override
    public Optional<MessageInfo> findMessageById(UUID messageId) {
        return messageRepo.findById(messageId).map(MessageInfo::new);
    }

    // update를 해도 순서는 바뀌지않음 생성일자로 정렬
    @Override
    public List<MessageInfo> findMessageBetweenUsers(UUID userId1, UUID userId2) {
        return messageRepo.findAllByBetweenUserIds(userId1, userId2)
                .stream().sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfo::new).collect(Collectors.toList());
    }

    @Override
    public List<MessageInfo> findChannelMessage(UUID channelId) {
        return messageRepo.findAllByChannelId(channelId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfo::new).collect(Collectors.toList());
    }

    // Message Update
    @Override
    public Optional<MessageInfo> updateMessage(UUID messageId, String newContent) {
        if (newContent == null || newContent.isBlank()) {
            deleteMessage(messageId);
            return Optional.empty();
        }

        return messageRepo.findById(messageId).map(message -> {
            message.updateContent(newContent);
            messageRepo.save(message);
            return new MessageInfo(message);
        });
    }

    // Message Delete
    @Override
    public boolean deleteMessage(UUID id) {
        if (messageRepo.findById(id).isPresent()) {
            messageRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
