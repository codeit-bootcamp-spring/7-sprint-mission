package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BinaryContentRepository {
    public BinaryContent createBinaryContent(BinaryContent binaryContent);
    public Optional<BinaryContent> readBinaryContent(UUID binaryContentId);
    public List<BinaryContent> readAllBinaryContent();
    public boolean isBinaryContentExist(UUID binaryContentId);
    public void deleteBinaryContent(UUID binaryContentId);


    public void resetBinaryContentRepository();
}
