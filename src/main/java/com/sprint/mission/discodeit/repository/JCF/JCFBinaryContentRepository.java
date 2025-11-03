package com.sprint.mission.discodeit.repository.JCF;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.InterfaceBinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository  implements InterfaceBinaryContentRepository {
    private final Map<UUID, UserStatus> data;

    public JCFBinaryContentRepository()  {
        this.data = new HashMap<>();
    }

    @Override
    public void save(BinaryContent model) {

    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<BinaryContent>> findAll() {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }
}
