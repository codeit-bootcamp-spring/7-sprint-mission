package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.MessageService;

import java.util.HashMap;
import java.util.Map;

public class JCFMessageService<T extends Receivable> implements MessageService<T> {
    private Map<User, Message<T>> data = new HashMap<>();

    @Override
    public void sendMessage(User sender, T receiver, String message) {
        Message<T> newMessage = (Message<T>) new Message<>(sender, receiver, message);
        data.put(sender, newMessage);
        newMessage.display();
    }
}
