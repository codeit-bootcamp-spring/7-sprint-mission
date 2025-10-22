package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> messages = new ArrayList<>();

    private static final JCFMessageService singleton = new JCFMessageService();
    private JCFMessageService() {}
    public static JCFMessageService getInstance() {
        return singleton;
    }

    @Override
    public Message insert(Channel channel, String message) {
        Message newMessage = new Message(channel,message);
        messages.add(newMessage);
        return newMessage;
    }

    @Override
    public List<Message> findAll() {
        return List.copyOf(messages);
    }

    @Override
    public Message findById(UUID id) {
        return messages.stream().filter(
                message -> message.getId().equals(id)).findFirst().orElseThrow(
                () -> new RuntimeException("해당 ID 를 가진 Message를 찾을 수 없습니다:" + id)
        );
    }
    private boolean chkWriter(User writer){
        return JCFUserService.loginUser.getId().equals(writer.getId());
    }
    @Override
    public Message update(UUID id, String content) {
        Message message = findById(id);

        if(chkWriter(message.getSpeaker())){
            message.setContent(content);
            message.setUpdatedAt(System.currentTimeMillis());
        }else{
            throw new RuntimeException("수정 권한 없음");
        }

        return message;
    }

    @Override
    public Message delete(UUID id) {
        Message message = findById(id);
        if(chkWriter(message.getSpeaker())){
            messages.remove(message);
        }else{
            throw new RuntimeException("삭제 권한 없음");
        }
        return message;
    }
}
