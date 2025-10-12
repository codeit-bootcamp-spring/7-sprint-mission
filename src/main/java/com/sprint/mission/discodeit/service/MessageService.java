package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MessageService<T> {
    void createMessage(User user, T receiver, String content);
    Message getLastestMessage(User user, T receiver);
    List<Message> getMessagesBetween(User user1, User user2);
    List<Message> getAllMessagesByUser(User user);
    List<Message> getAllByChannel(Channel channel);
    void updateMessage(UUID id, String content);
    void deleteMessage(UUID id);
    void delMessageByUser(User user);
}
