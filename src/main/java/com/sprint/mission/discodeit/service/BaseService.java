package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
* T는 엔티티
* ID는 UUID를 뜻함
* */
public interface BaseService <T>{

    void save(T entity);

    void remove(T entity);

    T findById(UUID id);

    List<T> findAll();
}
