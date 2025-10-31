package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.List;
import java.util.UUID;

public class FileBinaryContentRepository extends BaseFileRepository<BinaryContent> implements BinaryContentRepository {
    public FileBinaryContentRepository() {
        super(BinaryContent.class);
    }

    //저장
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        saveToFile(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public List<BinaryContent> findAll() {
        return findAllFiles();
    }

    @Override
    public BinaryContent findById(UUID binaryId) {
        return loadFromFile(binaryId);
    }

    @Override
    public BinaryContent delete(UUID binaryId) {
        BinaryContent binaryContent = loadFromFile(binaryId);
        deleteFile(binaryId);
        return binaryContent;
    }
}
