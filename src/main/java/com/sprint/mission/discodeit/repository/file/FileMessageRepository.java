package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository extends BaseFileRepository<Message> implements MessageRepository {
    public FileMessageRepository(RepositoryProperties repositoryProperties) {
        super(Message.class, repositoryProperties);
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

    //id 로 조회
    @Override
    public Optional<Message> findById(UUID id) {
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
    public void update(UUID id, String content, List<UUID> attachmentIds) {
        loadFromFile(id).ifPresent(message -> {
            message.update(content, attachmentIds);
            saveToFile(id, message);
        });
    }

    //메세지 삭제
    @Override
    public void delete(UUID id) {
        deleteFile(id);
    }

    //채널의 마지막 메세지
    @Override
    public Optional<Message> findLastMessageByChannelId(UUID channelId) {
        return findAllFiles().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt));
    }

    @Override
    public boolean existsById(UUID id) {
        return fileExistsById(id);
    }
}
