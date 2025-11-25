package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.BinaryContentEntity;
import com.sprint.mission.discodeit.repository.jpainterface.JpaBinaryContentRepository;
import com.sprint.mission.discodeit.service.mapper.BinaryContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BinaryContentRepositoryAdapter implements BinaryContentRepository {

    private final JpaBinaryContentRepository jpa;
    private final BinaryContentMapper mapper;

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        BinaryContentEntity save = jpa.save(mapper.toBinaryContentEntity(binaryContent));
        return mapper.toBinaryContent(save);
    }

    @Override
    public void delete(BinaryContent binaryContent) {
        jpa.delete(mapper.toBinaryContentEntity(binaryContent));
    }

    @Override
    public Optional<BinaryContent> findById(String id) {
        return jpa.findById(id).map(mapper::toBinaryContent);
    }

    @Override
    public List<BinaryContent> findAll() {
        return jpa.findAll().stream().map(mapper::toBinaryContent).toList();
    }
}
