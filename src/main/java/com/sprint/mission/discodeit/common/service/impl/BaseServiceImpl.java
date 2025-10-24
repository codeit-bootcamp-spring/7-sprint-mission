package com.sprint.mission.discodeit.common.service.impl;

import com.sprint.mission.discodeit.common.utils.Deletable;
import com.sprint.mission.discodeit.common.utils.Identifiable;
import com.sprint.mission.discodeit.common.repository.BaseRepository;
import com.sprint.mission.discodeit.common.service.BaseService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * BaseService 인터페이스의 공통 구현 로직을 담고 있는 추상 클래스입니다.
 * 구체적인 서비스 클래스가 도메인 특화 비즈니스 로직에만 집중할 수 있도록 돕습니다.
 *
 * @param <T>  엔티티 타입
 * @param <ID> 엔티티의 ID 타입
 * @param <R>  해당 엔티티를 다루는 {@link BaseRepository}의 하위 타입
 */
public abstract class BaseServiceImpl<T extends Identifiable<ID> & Deletable, ID, R extends BaseRepository<T, ID>>
        implements BaseService<T, ID> {

    protected final R repository;

    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        repository.save(entity);
        return entity;
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .peek(repository::save)
                .collect(Collectors.toList());
    }

    @Override
    public T findById(ID id) {
        // Repository로부터 Optional<T>를 받아, 비어있을 경우 예외를 던지는 것은 서비스 계층의 책임입니다.
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 데이터를 찾을 수 없습니다."));
    }

    @Override
    public T findByIdNonDel(ID id) {
        // Repository는 삭제된 데이터를 이미 걸러냈으므로, Service는 데이터가 없다는 사실에만 집중하여 예외를 처리합니다.
        return repository.findByIdNonDel(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 데이터를 찾을 수 없거나 이미 삭제되었습니다."));
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByIdNonDel(ID id) {
        return repository.existsByIdNonDel(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAllNonDel() {
        return repository.findAllNonDel();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public List<T> findAllByIdNonDel(Iterable<ID> ids) {
        return repository.findAllByIdNonDel(ids);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countNonDel() {
        return repository.countNonDel();
    }

    @Override
    public void deleteById(ID id) {
        if (!existsById(id)) {
            throw new NoSuchElementException("삭제할 데이터가 존재하지 않습니다.");
        }
        repository.deleteById(id);
    }

    @Override
    public void deleteAllById(Iterable<ID> ids) {
        // deleteAllById는 여러 건을 삭제하므로, 일부가 존재하지 않더라도 예외를 던지지 않고 존재하는 것만 지우는 것이 일반적인 정책입니다.
        // 만약 모든 ID가 반드시 존재해야 한다면, 여기서 추가적인 검증 로직이 필요합니다.
        repository.deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void softDeleteById(ID id) {
        // [개선된 부분] 'find-modify-save' 패턴에서 findByIdNonDel을 사용합니다.
        // 1. Find: 삭제되지 않은 엔티티를 조회합니다. (없거나 이미 삭제되었다면 여기서 예외 발생)
        T entity = findByIdNonDel(id);

        // 2. Modify: 엔티티의 상태 변경 책임을 엔티티 자신에게 위임합니다.
        entity.softDelete();

        // 3. Save: 변경된 상태를 저장소에 다시 저장(업데이트)합니다.
        repository.save(entity);
    }

    @Override
    public void softDeleteAll() {
        // [개선된 부분] 삭제되지 않은 모든 엔티티를 조회하여 처리하는 것이 더 효율적입니다.
        List<T> nonDeletedEntities = repository.findAllNonDel();
        nonDeletedEntities.forEach(T::softDelete);
        repository.saveAll(nonDeletedEntities);
    }

    /**
     * 논리적으로 삭제 처리된 모든 데이터를 영구적으로 제거하는 비즈니스 로직을 수행합니다.
     * 데이터 로깅처리가 완료된 이후 실행하여야합니다.
     */
    @Override
    public void deleteAllByIsDel() {
        repository.deleteAllByIsDel();
    }
}