package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.*;
import com.sprint.mission.discodeit.exception.NotFound;
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

    // 유저 및 채널 찾기
    private User findUser(UUID userId) {
        return userService.findUserEntityById(userId)
                .orElseThrow(() -> new NotFound("user not found"));
    }
    private Channel findChannel(UUID channelId) {
        return channelService.findChannelEntityById(channelId)
                .orElseThrow(() -> new NotFound("channel not found"));
    }

    // Message Create
    @Override
    public MessageInfo createDirectMessage(UUID authorId, UUID receiverId, String content) {
        User author = findUser(authorId);
        User receiver = findUser(receiverId);
        Message message = new Message(author, receiver, content);
        data.put(message.getId(), message);
        return new MessageInfo(message);
    }

    @Override
    public MessageInfo createChannelMessage(UUID authorId, UUID channelId, String content) {
        User author = findUser(authorId);
        Channel channel = findChannel(channelId);
        Message message = new Message(author, channel, content);
        data.put(message.getId(), message);
        return new MessageInfo(message);
    }


    // Message Read
    @Override
    public Optional<MessageInfo> findMessageById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId)).map(MessageInfo::new);
    }

    // update를 해도 순서는 바뀌지않음 생성일자로 정렬
    @Override
    public List<MessageInfo> findMessageBetweenUsers(UUID userId1, UUID userId2) {
        return data.values().stream()
                .filter(m -> m.getType() == Message.MessageType.DIRECT)
                .filter(m ->
                        (m.getAuthor().getId().equals(userId1) && m.getReceiver().getId().equals(userId2)) ||
                        (m.getAuthor().getId().equals(userId2) && m.getReceiver().getId().equals(userId1)))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfo::new).collect(Collectors.toList());
    }

    @Override
    public List<MessageInfo> findChannelMessage(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getType() == Message.MessageType.CHANNEL)
                .filter(m -> m.getChannel().getId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfo::new).collect(Collectors.toList());

    }

    // Message Update
    @Override
    public Optional<MessageInfo> updateMessage(UUID id, String newContent) {
        if (newContent.isEmpty()) {
            deleteMessage(id);
            return Optional.empty();
        }
        Optional<Message> messageOp = Optional.ofNullable(data.get(id));

        return messageOp.map(message -> {
            message.updateContent(newContent);
            return new MessageInfo(message);
        });
    }

    // Message Delete
    @Override
    public boolean deleteMessage(UUID id) {
        return data.remove(id) != null;
    }
}


// https://velog.io/@wonizizi99/Optional%EC%9D%98-orElseorElseThrow-%EC%82%AC%EC%9A%A9%EB%B2%95