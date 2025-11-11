package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent, UUID> implements BinaryContentRepository {


    private final String filePath;

    public FileBinaryContentRepository(String fileDirectory) {
        this.filePath = fileDirectory + File.separator + "binaryContent.ser";
    }

    @Override
    protected String getFilePath() {
        return filePath;
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
