package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

/**
 * ✅ FileMessageService
 *  - Message 생성/수정/삭제 등의 비즈니스 로직 담당
 *  - Repository를 통해 파일 기반으로 데이터 영속화
 */
public class FileMessageService implements MessageService {
    private final MessageRepository repo;

    public FileMessageService() { this.repo = new FileMessageRepository(); }
    public FileMessageService(MessageRepository repo) { this.repo = repo; }

    @Override
    public Message create(UUID senderId, UUID channelId, String content) {
        if (senderId == null) throw new IllegalArgumentException("senderId null");
        if (channelId == null) throw new IllegalArgumentException("channelId null");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content 비어있음");
        return repo.save(new Message(senderId, channelId, content));
    }

    @Override public Message read(UUID id) { return repo.findById(id); }
    @Override public List<Message> readAll() { return repo.findAll(); }

    @Override
    public Message updateContent(UUID id, String newContent) {
        if (newContent == null || newContent.isBlank()) throw new IllegalArgumentException("newContent 비어있음");
        Message m = repo.findById(id);
        if (m != null) { m.updateContent(newContent); repo.save(m); }
        return m;
    }

    @Override public boolean delete(UUID id) { return repo.deleteById(id); }
}