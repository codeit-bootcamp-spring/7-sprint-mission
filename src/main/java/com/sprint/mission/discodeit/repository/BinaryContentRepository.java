package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {

    void save(BinaryContent binaryContent);

    BinaryContent findById(UUID id);

    List<BinaryContent> findAll();

    void delete(UUID id);

    /** 메시지 삭제 시 관련 파일들 삭제 */
    void deleteByIds(List<UUID> idList);
}
