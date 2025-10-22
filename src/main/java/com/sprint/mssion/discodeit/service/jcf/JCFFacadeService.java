package com.sprint.mssion.discodeit.service.jcf;

// 1. 파사드 패턴
// - 서비스 순환 참조 발생 -> 서비스 간에 의존 의문 -> 컨트롤러로 대체?
// - 파사드 패턴으로 상위 서비스 만들어서 거기서 처리
// - 작은 프로젝트 간에는 할 필요가 없으나, 해봄
// - 너무 많은 기능이 담기면, god service가 될 수 있다

import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.service.ChannelService;
import com.sprint.mssion.discodeit.service.MessageService;
import com.sprint.mssion.discodeit.service.UserService;

import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFFacadeService {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public JCFFacadeService(UserService userService, ChannelService channelService, MessageService messageService) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageService = messageService;
    }

    public Message createMessageWithRelation(UUID userId, UUID channelId, String content) {
        if (!userService.isExistsUser(userId)) throw new NoSuchElementException("찾을 수 없는 유저입니다.");
        if (!channelService.isExistsChannel(channelId)) throw new NoSuchElementException("찾을 수 없는 채널입니다.");
        userService.addChannelToUser(userId, channelId); // 유저가 채널에 속해있지 않다면, 채널에 속하도록 한다.
        return messageService.createMessage(content, channelId, userId);
    }

    public void deleteMessageWithRelation(UUID messageId) {
        Message message = messageService.getMessage(messageId);
        UUID userId = message.getUserId();
        UUID channelId = message.getChannelId();
        if (!userService.isExistsUser(userId)) throw new NoSuchElementException("찾을 수 없는 유저입니다.");
        if (!channelService.isExistsChannel(channelId)) throw new NoSuchElementException("찾을 수 없는 채널입니다.");
        messageService.deleteMessage(messageId);
    }

    public void addChannelToUserWithRelation(UUID userId, UUID channelId) {
        if (!userService.isExistsUser(userId)) {
            throw new NoSuchElementException("찾을 수 없는 유저: " + userId);
        }
        if (!channelService.isExistsChannel(channelId)) {
            throw new NoSuchElementException("찾을 수 없는 채널: " + channelId);
        }
        userService.addChannelToUser(userId, channelId);
    }


    public void deleteChannelWithRelation(UUID channelId) {
        channelService.deleteChannel(channelId);
        userService.removeChannelFromAllUsers(channelId);
        messageService.deleteMessagesByChannel(channelId);
    }

    public void deleteUserWithRelation(UUID userId) {
        userService.deleteUser(userId);
        messageService.deleteMessagesByUser(userId);
    }
}
