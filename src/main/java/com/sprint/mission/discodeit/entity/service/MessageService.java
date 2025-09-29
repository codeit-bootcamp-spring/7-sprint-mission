package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    void create(User sender, User receiver, String message);
    void read(Message message);
    void readAll(List<Message> messages);
    void update(Message message);
    void delete(Message message);
}
