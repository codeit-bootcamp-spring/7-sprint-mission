package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository extends JCFBaseRepository<Message> implements MessageRepository {
    //메세지 전부 조회
    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    //말한 사람 메세지 조회
    @Override
    public List<Message> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(m -> m.getSpeakerId().equals(userId))
                .toList();
    }

    //같은 채널 메세지 모두 조회
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();
    }

    //내용이 포함된 메세지들 찾기
    @Override
    public List<Message> findByContentContaining(String searchText) {
        return data.values().stream()
                .filter(m -> m.getContent().contains(searchText))
                .toList();
    }

    //메세지 id로 조회
    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    //메세지 저장
    @Override
    public Message save(Message message) {
        beforeModify();
        data.put(message.getId(), message);
        return message;
    }

    //메세지 수정
    @Override
    public void update(UUID id, String content, List<UUID> attachmentIds) {
        beforeModify();
        data.get(id).update(content, attachmentIds);
    }

    //메세지 삭제
    @Override
    public void delete(UUID id) {
        beforeModify();
        data.remove(id);
    }

    //한 채널에서 가장 마지막으로 보낸 메세지 찾기
    @Override
    public Optional<Message> findLastMessageByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt));
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
