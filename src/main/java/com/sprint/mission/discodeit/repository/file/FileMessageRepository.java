package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.nio.file.Path;
import java.util.*;
/**
 * ✅ FileMessageRepository
 *  - Message 데이터의 파일 입출력을 담당
 *  - 각 메시지를 Map<UUID, Message> 형태로 관리
 */
public class FileMessageRepository extends AbstractFileMapRepository<UUID, Message> implements MessageRepository {
    /**
     * 생성자: 파일 경로를 받아서, 기존 데이터가 있으면 로드함
     *
     */
    public FileMessageRepository() { super(FilePaths.MESSAGES); }
    public FileMessageRepository(Path path) { super(path); }

    @Override public Message save(Message e) { data.put(e.getId(), e); persist(); return e; }
    @Override public Message findById(UUID id) { return data.get(id); }
    @Override public List<Message> findAll() { return new ArrayList<>(data.values()); }
    @Override public boolean deleteById(UUID id) {
        boolean removed = (data.remove(id) != null);
        if (removed) persist();
        return removed;
    }
}
