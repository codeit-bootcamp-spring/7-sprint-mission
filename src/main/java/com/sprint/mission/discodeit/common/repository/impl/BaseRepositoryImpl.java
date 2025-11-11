package com.sprint.mission.discodeit.common.repository.impl;

import com.sprint.mission.discodeit.common.utils.Deletable;
import com.sprint.mission.discodeit.common.utils.Identifiable;
import com.sprint.mission.discodeit.common.repository.BaseRepository;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Java Collections Framework(메모리)를 기반으로 BaseRepository 인터페이스를 구현한 추상 클래스입니다.
 * 데이터 저장을 위해 HashMap을 사용합니다.
 *
 * @param <T>  Identifiable 및 Deletable 인터페이스를 구현한 엔티티 타입
 * @param <ID> 해당 엔티티의 ID 타입
 */
@Getter
public abstract class BaseRepositoryImpl<T extends Identifiable<ID> & Deletable, ID> implements BaseRepository<T, ID> {

    protected final Map<ID, T> dataMap = new ConcurrentHashMap<>();

    @Override
    public void save(T entity) {
        // HashMap의 put 메서드는 키가 존재하면 값을 덮어쓰므로, 생성(Create)과 수정(Update)을 모두 처리합니다.
        dataMap.put(entity.getId(), entity);
    }

    @Override
    public void saveAll(Iterable<T> entities) {
        // 전달받은 모든 엔티티에 대해 save 메서드를 반복 호출합니다.
        entities.forEach(this::save);
    }

    @Override
    public Optional<T> findById(ID id) {
        // dataMap.get()은 키가 없으면 null을 반환하며, Optional.ofNullable()을 통해 안전하게 감싸줍니다.
        return Optional.ofNullable(dataMap.get(id));
    }

    @Override
    public Optional<T> findByIdNonDel(ID id) {
        // ID를 기반으로 데이터를 우선 찾습니다.
        T entity = dataMap.get(id);

        // 데이터가 존재하고(not null) 동시에 삭제되지 않은(isDeleted=false) 경우에만 Optional로 감싸 반환합니다.
        if (entity != null && !entity.isDeleted()) {
            return Optional.of(entity);
        }

        // 데이터가 없거나, 이미 삭제된 상태라면 빈 Optional을 반환합니다.
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        // 내부 데이터(dataMap.values())가 외부에서 직접 수정되는 것을 방지하기 위해
        // 방어적 복사(Defensive Copy)를 통해 새로운 리스트를 생성하여 반환합니다.
        return new ArrayList<>(dataMap.values());
    }


    @Override
    public List<T> findAllNonDel() {
        // 메모리에 있는 모든 데이터를 스트림으로 처리하여 삭제되지 않은(isDeleted=false) 엔티티만 필터링합니다.
        return dataMap.values().stream()
                .filter(entity -> !entity.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(id -> Optional.ofNullable(dataMap.get(id)))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findAllByIdNonDel(Iterable<ID> ids) {
        // 먼저 ID 목록으로 모든 엔티티를 조회한 후,
        List<T> foundEntities = findAllById(ids);

        // 메모리 상에서 삭제되지 않은 엔티티만 필터링합니다.
        return foundEntities.stream()
                .filter(entity -> !entity.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        // HashMap의 size() 메서드는 O(1) 시간 복잡도를 가지므로 매우 효율적입니다.
        return dataMap.size();
    }

    @Override
    public long countNonDel() {
        // 전체 데이터를 스트림으로 순회하며 조건에 맞는 개수를 계산합니다.
        // 데이터가 매우 많을 경우 성능에 영향을 줄 수 있습니다.
        return dataMap.values().stream()
                .filter(entity -> !entity.isDeleted())
                .count();
    }

    @Override
    public boolean existsById(ID id) {
        // HashMap의 containsKey()는 O(1) 시간 복잡도로 매우 빠르게 존재 여부를 확인합니다.
        return dataMap.containsKey(id);
    }

    @Override
    public boolean existsByIdNonDel(ID id) {
        // [개선된 부분] findByIdNonDel을 호출하는 대신, 직접 데이터를 조회하여 확인하는 것이 더 효율적입니다.
        T entity = dataMap.get(id);
        // 데이터가 존재하고, 동시에 논리적으로 삭제되지 않았는지 확인합니다.
        return entity != null && !entity.isDeleted();
    }

    @Override
    public void deleteById(ID id) {
        dataMap.remove(id);
    }

    @Override
    public void deleteAll() {
        // 모든 데이터를 초기화합니다.
        dataMap.clear();
    }

    @Override
    public void deleteAllById(Iterable<ID> ids) {
        ids.forEach(this::deleteById);
    }

    /**
     * 저장소에 있는 모든 엔티티 중, 논리적으로 삭제된(isDeleted=true) 상태의 모든 데이터를 물리적으로 제거합니다.
     */
    @Override
    public void deleteAllByIsDel() {
        // 1. dataMap에서 논리적으로 삭제된(isDeleted=true) 엔티티들의 ID 목록을 찾습니다.
        //    - 스트림을 중간에 toList()로 수집하여 ConcurrentModificationException을 방지합니다.
        List<ID> idsToDelete = dataMap.values().stream()
                .filter(Deletable::isDeleted)
                .map(Identifiable::getId)
                .collect(Collectors.toList());

        // 2. 찾아낸 ID 목록을 순회하며 dataMap에서 해당 엔티티를 제거합니다.
        idsToDelete.forEach(dataMap::remove);
    }

    @Override
    public void loadDataMap(Map<ID, T> dataMap) {
        this.dataMap.clear();
        this.dataMap.putAll(dataMap);
    }
}