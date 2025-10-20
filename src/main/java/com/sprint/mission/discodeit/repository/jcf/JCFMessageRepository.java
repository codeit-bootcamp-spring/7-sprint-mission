package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JCFMessageRepository
 * -----------------
 * Java Collection Framework(JCF)을 이용해 메시지를 메모리에 저장하는 구현체입니다.
 *
 * 실제 DB를 사용하지 않고 List<Message>를 저장소로 활용합니다.
 */
public class JCFMessageRepository implements MessageRepository {
    private final List<Message> messageStore = new ArrayList<>();

    @Override
    public void save(Message message) {
        messageStore.add(message);
    }

    @Override
    public List<Message> findAll() {
        // 외부에서 리스트를 수정하지 못하도록 복사본 반환
        return new ArrayList<>(messageStore);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return messageStore.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public void update(Message updatedMessage) {
        for (int i = 0; i < messageStore.size(); i++) {
            if (messageStore.get(i).getId().equals(updatedMessage.getId())) {
                messageStore.set(i, updatedMessage);
                break;
            }
        }
    }

    @Override
    public void deleteById(UUID id) {
        messageStore.removeIf(m -> m.getId().equals(id));
    }

    @Override
    public void deleteByUser(User user) {
        // 특정 유저가 보낸 메시지 전부 삭제
        messageStore.removeIf(m -> m.getSenderId().equals(user.getId()));
    }
}
