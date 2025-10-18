package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.UUID;

public interface Repository<T, ID>{

    T save(T entity);
    T findById(ID id);
    List<T> findAll();
    void delete(ID id);
}
