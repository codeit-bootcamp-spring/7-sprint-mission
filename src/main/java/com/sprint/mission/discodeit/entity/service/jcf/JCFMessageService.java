package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.MessageService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final List<Message> messages = new LinkedList<>();

    public JCFMessageService() {

    }




    @Override
    public void create(User sender, User receiver, String message) {

    }

    @Override
    public void read(Message message) {

    }

    @Override
    public void readAll(List<Message> messages) {

    }

    @Override
    public void update(Message message) {

    }

    @Override
    public void delete(Message message) {

    }
}
