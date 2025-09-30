package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Utils.Deletable;
import com.sprint.mission.discodeit.Utils.Identifiable;
import com.sprint.mission.discodeit.repository.BaseRepository;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
public abstract class JCFBaseService<T extends Identifiable<ID> & Deletable, ID, R extends BaseRepository<T, ID>>
        implements BaseService<T, ID> {

    protected final R repository;

    protected JCFBaseService(R repository) {
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
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 데이터를 찾을 수 없습니다: " + id));
    }

    @Override
    public T findByIdNonDel(ID id) {
        // 1. ID로 데이터를 우선 조회합니다.
        T entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 데이터를 찾을 수 없습니다: " + id));

        // 2. 서비스 계층에서 "삭제된 데이터는 없는 데이터로 취급한다"는 비즈니스 규칙을 적용합니다.
        if (entity.isDeleted()) {
            throw new NoSuchElementException("ID에 해당하는 데이터는 존재하지만, 삭제된 상태입니다: " + id);
        }

        return entity;
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByIdNonDel(ID id) {
        // 데이터를 직접 조회하여, 존재하는 동시에 삭제되지 않았는지 확인합니다.
        Optional<T> optionalEntity = repository.findById(id);
        return optionalEntity.isPresent() && !optionalEntity.get().isDeleted();
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
        // 데이터를 삭제하기 전에, 실제로 존재하는지 먼저 확인하는 방어 로직을 추가합니다.
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("삭제할 데이터가 존재하지 않습니다: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void deleteAllById(Iterable<ID> ids) {
        repository.deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void softDeleteById(ID id) {
        // 'find-modify-save' 패턴을 사용하여 논리적 삭제를 구현합니다.
        // 1. Find: ID로 엔티티를 조회합니다. (없으면 findById 내부에서 예외 발생)
        T entity = findById(id);

        // 2. Modify: 엔티티의 상태 변경 책임을 엔티티 자신에게 위임합니다.
        entity.softDelete();

        // 3. Save: 변경된 상태를 저장소에 다시 저장(업데이트)합니다.
        repository.save(entity);
    }

    @Override
    public void softDeleteAll() {
        List<T> entities = repository.findAll();
        entities.forEach(T::softDelete);
        repository.saveAll(entities);
    }
}