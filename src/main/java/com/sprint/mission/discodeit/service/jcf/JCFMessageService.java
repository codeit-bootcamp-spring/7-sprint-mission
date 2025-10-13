package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.MessageInfo;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

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

    // Find User, Channel
    private User FindUser(UUID userId) {
        return userService.findUserEntityById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));
    }
    private Channel FindChannel(UUID channelId) {
        return channelService.findChannelEntityById(channelId)
                .orElseThrow(() -> new RuntimeException("채널을 찾을 수 없음"));
    }


    // Message Create
    @Override
    public MessageInfo createDirectMessage(UUID authorId, UUID receiverId, String content) {
        User author = FindUser(authorId);
        User receiver = FindUser(receiverId);
        Message message = new Message(author, receiver, content);
        data.put(message.getId(), message);
        return new MessageInfo(message);
    }

    @Override
    public MessageInfo createChannelMessage(UUID authorId, UUID channelId, String content) {
        User author = FindUser(authorId);
        Channel channel = FindChannel(channelId);
        Message message = new Message(author, channel, content);
        data.put(message.getId(), message);
        return new MessageInfo(message);
    }


    // Message Read
    @Override
    public Optional<MessageInfo> findMessageById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId)).map(MessageInfo::new);
    }

    @Override
    public List<MessageInfo> findDMBetweenUsers(UUID userId1, UUID userId2) {
        return data.values().stream()
                .filter(m -> m.getType() == Message.MessageType.DIRECT)
                .filter(m ->
                        (m.getAuthor().getId().equals(userId1) && m.getReceiver().getId().equals(userId2)) ||
                        (m.getAuthor().getId().equals(userId2) && m.getReceiver().getId().equals(userId1)))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfo::new).collect(Collectors.toList());
    }

    @Override
    public List<MessageInfo> findCMByChannel(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getType() == Message.MessageType.CHANNEL)
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(MessageInfo::new).collect(Collectors.toList());

    }

    // Message Update
    @Override
    public Optional<MessageInfo> update(UUID id, String newContent) {
        Optional<Message> messageOp = Optional.ofNullable(data.get(id));

        return messageOp.map(message -> {
            message.updateContent(newContent);
            return new MessageInfo(message);
        });
    }

    // Message Delete
    @Override
    public boolean deleteMessage(UUID id) {
        if (data.remove(id) != null) {
            System.out.println("메시지 삭제 성공");
            return true;
        } else {
            System.out.println("해당 메시지가 존재하지 않음");
            return false;
        }
    }
}


// https://velog.io/@wonizizi99/Optional%EC%9D%98-orElseorElseThrow-%EC%82%AC%EC%9A%A9%EB%B2%95