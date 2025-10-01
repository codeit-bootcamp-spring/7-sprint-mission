package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    Map<UUID, Message> Log = new HashMap<>();

    @Override
    public Message create(Message message) {
        Log.put(message.getId(), message);
        return message;
    }

    @Override
    public Message read(UUID id) {      // 한 사람의 채팅로그
        return Log.get(id);
    }

    @Override
    public List<Message> readAll() {    // 두 사람의 대화로그 혹은 채널의 전체로그
        return null;
    }

    @Override
    public Message update(UUID id, Message message) { // 한 사람의 대화 로그 중 수정
        return null;
    }

    @Override
    public void delete(UUID id) {   // 대화 로그 하나 삭제

    }
}
