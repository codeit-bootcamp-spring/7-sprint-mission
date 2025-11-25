package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.entity.ReadStatusEntity;
import com.sprint.mission.discodeit.repository.jpainterface.JpaReadStatusRepository;
import com.sprint.mission.discodeit.service.mapper.ReadStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ReadStatusRepositoryAdapter implements ReadStatusRepository {

    private final JpaReadStatusRepository jpa;
    private final ReadStatusMapper mapper;

    @Override
    public void save(ReadStatus readStatus) {
        jpa.save(mapper.toReadStatusEntity(readStatus));
    }

    @Override
    public void delete(ReadStatus readStatus) {
        jpa.delete(mapper.toReadStatusEntity(readStatus));
    }

    @Override
    public Optional<ReadStatus> findById(String id) {
        return jpa.findById(id).map(mapper::toReadStatus);
    }



    @Override
    public List<ReadStatus> findAll() {
        return jpa.findAll().stream().map(mapper::toReadStatus).toList();
    }
}
