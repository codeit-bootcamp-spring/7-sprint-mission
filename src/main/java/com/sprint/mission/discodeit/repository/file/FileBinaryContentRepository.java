package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent, UUID> implements BinaryContentRepository {

    private static final String FILE_PATH = "data" + File.separator + "binaryContent.ser";

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }

    @Override
    protected UUID getId(BinaryContent binaryContent) {
        return binaryContent.getId();
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> BinaryContentIds) {
        return findAll().stream()
                .filter(binaryContent -> BinaryContentIds.contains(binaryContent.getId()))
                .toList();
    }
}
