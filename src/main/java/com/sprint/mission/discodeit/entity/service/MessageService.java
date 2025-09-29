package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void  create(User sender, User receiver, String message);
    void read(UUID messageId);
    void readAll();
    void update(UUID messageId,String content);
    void delete(UUID messageId);
}
