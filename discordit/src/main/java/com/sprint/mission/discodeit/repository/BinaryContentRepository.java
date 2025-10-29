package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.content.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent content);
    void saveAll(List<BinaryContent> contents);

    void delete(BinaryContent content);
    void deleteAll(List<BinaryContent> attachments);

    BinaryContent findById(UUID uuid);


}
