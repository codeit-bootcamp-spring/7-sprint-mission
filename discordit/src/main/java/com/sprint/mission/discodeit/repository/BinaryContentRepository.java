package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent content);
    void saveAll(List<BinaryContent> contents);

    void delete(BinaryContent content);
    void deleteAll(List<BinaryContent> attachments);

    Optional<BinaryContent> findById(UUID uuid);

    List<BinaryContent> findAll();

    void deleteByID(UUID uuid);
}
