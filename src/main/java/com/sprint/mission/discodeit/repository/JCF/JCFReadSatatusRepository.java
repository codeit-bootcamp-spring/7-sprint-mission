package com.sprint.mission.discodeit.repository.JCF;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.InterfaceReadStatusRepository;

import java.util.*;

//@Repository
public class JCFReadSatatusRepository implements InterfaceReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadSatatusRepository()  {
        this.data = new HashMap<>();
    }

    @Override
    public void save(ReadStatus model) {

    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<ReadStatus>> findAll() {
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
