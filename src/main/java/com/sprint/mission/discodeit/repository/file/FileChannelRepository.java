package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.nio.file.Path;
import java.util.*;
/**
 * ✅ FileChannelRepository
 *  - Channel 데이터를 파일로 관리
 *  - ChannelService가 이를 통해 CRUD 수행
 */
public class FileChannelRepository extends AbstractFileMapRepository<UUID, Channel>
        implements ChannelRepository {
    /**
     * 생성자: 파일 경로를 받아서, 기존 데이터가 있으면 로드함
     *
     */
    public FileChannelRepository() { super(FilePaths.CHANNELS); }
    public FileChannelRepository(Path path) { super(path); }

    @Override public Channel save(Channel e) { data.put(e.getId(), e); persist(); return e; }
    @Override public Channel findById(UUID id) { return data.get(id); }
    @Override public List<Channel> findAll() { return new ArrayList<>(data.values()); }
    @Override public boolean deleteById(UUID id) {
        boolean removed = (data.remove(id) != null);
        if (removed) persist();
        return removed;
    }
}
