package com.sprint.mission.service.file;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.MessageService;

import java.util.List;

public class FileMessageService implements MessageService {
    @Override
    public void sendMessage(User sender, Receivable receiver, String message) {

    }

    @Override
    public <T extends Receivable> List<Message<T>> getBySenderAndReceiver(User sender, T receiver) {
        return List.of();
    }
}
