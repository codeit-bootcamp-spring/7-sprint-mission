package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

public class FileMessageRepository extends BaseFileRepository<Message> implements MessageRepository {
    //싱글톤 구현
    private final static FileMessageRepository instance = new FileMessageRepository();

    private FileMessageRepository() {
        super(Message.class);
    }

    public static FileMessageRepository getInstance() {
        return instance;
    }

    //메세지 모두 찾기
    @Override
    public List<Message> findAll() {
        return findAllFiles();
    }

    //한 유저가 보낸 메세지 모두 찾기
    @Override
    public List<Message> findAllByUserId(UUID userId) {
        return findAllFiles().stream()
                .filter(m -> m.getSpeakerId().equals(userId))
                .toList();
    }

    //한 채널 안에 있는 메세지 모두 찾기
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAllFiles().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();
    }

    //텍스트가 포함된 메세지 모두 찾기
    @Override
    public List<Message> findByContentContaining(String searchText) {
        return findAllFiles().stream()
                .filter(m -> m.getContent().contains(searchText))
                .toList();
    }

    @Override
    public Message findById(UUID id) {
        return loadFromFile(id);
    }

    //저장
    @Override
    public Message save(Message message) {
        saveToFile(message.getId(), message);
        return message;
    }

    //메세지 수정
    @Override
    public Message update(UUID id, String content) {
        Message message = loadFromFile(id);
        if(message == null){
            throw new RuntimeException("Message with id " + id + " not found");
        }
        message.update(content);
        saveToFile(message.getId(), message);
        return message;
    }

    //메세지 삭제
    @Override
    public Message delete(UUID id) {
        Message message = loadFromFile(id);
        deleteFile(id);
        return message;
    }
}
