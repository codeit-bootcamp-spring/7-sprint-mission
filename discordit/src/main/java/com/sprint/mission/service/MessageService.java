package com.sprint.mission.service;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageService {
    void sendMessage(User sender, Receivable receiver, String message);

    <T extends  Receivable> List<Message<T>> getBySenderAndReceiver(User sender, T receiver);
}
