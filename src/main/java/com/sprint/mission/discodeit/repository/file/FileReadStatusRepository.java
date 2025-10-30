package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FileReadStatusRepository extends BaseFileRepository<ReadStatus>
implements ReadStatusRepository {
    protected FileReadStatusRepository(Class<ReadStatus> type) {
        super(type);
    }

    //저장
    @Override
    public ReadStatus save(ReadStatus readStatus) {
        saveToFile(readStatus.getId(), readStatus);
        return readStatus;
    }

    //목록조회
    @Override
    public List<ReadStatus> findAll() {
        return findAllFiles();
    }

    //단일조회
    @Override
    public ReadStatus findById(UUID statusId) {
        return loadFromFile(statusId);
    }

    //삭제
    @Override
    public ReadStatus delete(UUID statusId) {
        ReadStatus readStatus = loadFromFile(statusId);
        deleteFile(statusId);
        return readStatus;
    }
}
