package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private List<Message> data;
    private final String filename = "messages";

    private static final FileMessageService singleton = new FileMessageService();
    private FileMessageService() {}
    public static FileMessageService getInstance() {
        return singleton;
    }

    @Override
    public List<Message> findAll() {
        try(ObjectInputStream is = FileInOutUtil.getInputStream(filename);){
            data = (ArrayList)is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public Message findById(UUID id) {
        data = findAll();
        return data.stream().filter(
                u -> u.getId().equals(id)).findFirst().orElseThrow(
                () -> new RuntimeException("해당 ID를 가진 message를 찾을 수 없습니다: " + id)
        );
    }

    @Override
    public Message insert(Channel channel, String message) {
        Message newMessage = new Message(channel,message);
        try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
            data = findAll();
            data.add(newMessage);
            os.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newMessage;
    }

    private boolean chkWriter(User writer){
        return FileUserService.loginUser.getId().equals(writer.getId());
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = findById(id);

        if(chkWriter(message.getSpeaker())){
            message.setContent(content);
            message.setUpdatedAt(System.currentTimeMillis());

            try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
                data = findAll();
                os.writeObject(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            throw new RuntimeException("수정 권한 없음");
        }

        return message;
    }

    @Override
    public Message delete(UUID id) {
        Message message = findById(id);
        if(chkWriter(message.getSpeaker())){
            data.remove(message);

            try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
                data = findAll();
                os.writeObject(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            throw new RuntimeException("삭제 권한 없음");
        }
        return message;
    }
}
