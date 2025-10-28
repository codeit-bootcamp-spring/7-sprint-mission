package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.messageDto.ChannelMessageCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.DirectMessageCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageInfoDto;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import com.sprint.mission.discodeit.service.*;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    // Message Create
    @Override
    public MessageInfoDto createDirectMessage(DirectMessageCreateRequestDto dto) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
        User author = userService.findUserEntityById(dto.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException("메시지를 보내는 사용자를 찾을 수 없음"));
        User receiver = userService.findUserEntityById(dto.getReceiverId())
                .orElseThrow(() -> new NoSuchElementException("메시지를 받을 사용자를 찾을 수 없음"));
        Message message = new Message(author, receiver, dto.getContent());
        data.put(message.getId(), message);
        return MessageInfoDto.from(message);

    }

    @Override
    public MessageInfoDto createChannelMessage(ChannelMessageCreateRequestDto dto) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new InvalidInputException("공백을 보낼 수 없음");
        }
            User author = userService.findUserEntityById(dto.getAuthorId())
                    .orElseThrow(() -> new NoSuchElementException("메시지를 보내는 사용자를 찾을 수 없음"));
            Channel channel = channelService.findChannelEntityById(dto.getChannelId())
                    .orElseThrow(() -> new NoSuchElementException("메시지를 받을 채널을 찾을 수 없음"));
            Message message = new Message(author, channel, dto.getContent());
            data.put(message.getId(), message);
            return MessageInfoDto.from(message);
    }

    // Message Read
    @Override
    public Optional<MessageInfoDto> findMessageById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId)).map(MessageInfoDto::from);
    }

    // update를 해도 순서는 바뀌지않음 생성일자로 정렬
    @Override
    public List<MessageInfoDto> findMessageBetweenUsers(UUID userId1, UUID userId2) {
        return data.values().stream()
                .filter(m -> m.getType() == MessageType.DIRECT)
                .filter(m ->
                        (m.getAuthor().getId().equals(userId1) && m.getReceiver().getId().equals(userId2)) ||
                                (m.getAuthor().getId().equals(userId2) && m.getReceiver().getId().equals(userId1)))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfoDto::from).collect(Collectors.toList());
    }

    @Override
    public List<MessageInfoDto> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getType() == MessageType.CHANNEL)
                .filter(m -> m.getChannel().getId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfoDto::from).collect(Collectors.toList());

    }

    // Message Update
    @Override
    public Optional<MessageInfoDto> updateMessage(UUID id, String newContent) {
        if (newContent == null || newContent.isBlank()) {
            deleteMessage(id);
            return Optional.empty();
        }
        Optional<Message> messageOp = Optional.ofNullable(data.get(id));

        return messageOp.map(message -> {
            message.updateContent(newContent);
            return MessageInfoDto.from(message);
        });
    }

    // Message Delete
    @Override
    public boolean deleteMessage(UUID id) {
        return data.remove(id) != null;
    }
}
