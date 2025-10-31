package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.List;
import java.util.UUID;

public class FileReadStatusRepository extends BaseFileRepository<ReadStatus>
implements ReadStatusRepository {
    public FileReadStatusRepository() {
        super(ReadStatus.class);
    }

    //저장
    @Override
    public ReadStatus save(ReadStatus readStatus) {
        saveToFile(readStatus.getId(), readStatus);
        return readStatus;
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
