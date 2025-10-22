package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCF_MessageService {
    private static JCF_MessageService messageService = new JCF_MessageService();
    public static JCF_MessageService getInstance() {
        return messageService;
    }

    public final Map<UUID, Message> data;

    private JCF_MessageService() {
        data = new HashMap<>();
    }

//    @Override
    public Message neoMessage(String msg) {
        Message message = new Message(msg);
        data.put(message.getId(), message);

//        System.out.println("\uD83D\uDC8C createMessage : " + msg); // 💌
        return message;
    }
}
