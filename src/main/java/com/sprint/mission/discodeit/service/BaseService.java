package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 모든 서비스 계층에서 공통적으로 사용될 기본 CRUD 기능을 정의하는 제네릭 인터페이스입니다.
 * Spring Data의 CrudRepository 인터페이스를 참고하여 설계되었습니다.
 *
 * @param <T>  서비스가 다룰 엔티티 타입
 * @param <ID> 엔티티의 ID 타입
 */
public interface BaseService<T, ID> {


    /**
     * 주어진 엔티티를 저장(Create 또는 Update)합니다.
     *
     * @param entity 저장할 엔티티
     * @return 저장된 엔티티 (ID 등이 부여된 최종 상태)
     */
    T save(T entity);

    /**
     * 주어진 여러 엔티티를 모두 저장합니다.
     *
     * @param entities 저장할 엔티티들의 Iterable
     * @return 저장된 엔티티들의 List
     */
    List<T> saveAll(Iterable<T> entities);

    /**
     * 고유 ID로 특정 엔티티를 조회합니다.
     *
     * @param id 조회할 엔티티의 ID
     * @return 조회된 엔티티 객체
     * @throws NoSuchElementException 해당 ID의 엔티티가 없을 경우
     */
    T findById(ID id);

    /**
     * 고유 ID로 삭제되지 않은 특정 엔티티를 조회합니다.
     *
     * @param id 조회할 엔티티의 ID
     * @return 조회된 엔티티 객체
     * @throws NoSuchElementException 해당 ID의 엔티티가 없거나 삭제된 상태일 경우
     */
    T findByIdNonDel(ID id);

    /**
     * 주어진 ID를 가진 엔티티가 존재하는지 확인합니다.
     *
     * @param id 확인할 엔티티의 ID
     * @return 존재하면 true, 아니면 false
     */
    boolean existsById(ID id);

    /**
     * 주어진 ID를 가진, 삭제되지 않은 엔티티가 존재하는지 확인합니다.
     *
     * @param id 확인할 엔티티의 ID
     * @return 존재하며 삭제되지 않았으면 true, 아니면 false
     */
    boolean existsByIdNonDel(ID id);

    /**
     * 모든 엔티티를 조회합니다.
     *
     * @return 모든 엔티티를 담은 List, 없으면 빈 리스트를 반환
     */
    List<T> findAll();

    /**
     * 삭제되지 않은 모든 엔티티를 조회합니다.
     *
     * @return 조건에 맞는 엔티티를 담은 List, 없으면 빈 리스트를 반환
     */
    List<T> findAllNonDel();

    /**
     * 주어진 여러 ID에 해당하는 모든 엔티티를 조회합니다.
     *
     * @param ids 조회할 엔티티 ID들의 Iterable
     * @return 조회된 엔티티들의 List
     */
    List<T> findAllById(Iterable<ID> ids);

    /**
     * 주어진 여러 ID에 해당하는, 삭제되지 않은 모든 엔티티를 조회합니다.
     *
     * @param ids 조회할 엔티티 ID들의 Iterable
     * @return 조건에 맞게 조회된 엔티티들의 List
     */
    List<T> findAllByIdNonDel(Iterable<ID> ids);

    /**
     * 전체 엔티티 수를 반환합니다.
     *
     * @return long 타입의 전체 엔티티 수
     */
    long count();

    /**
     * 삭제되지 않은 전체 엔티티 수를 반환합니다.
     *
     * @return long 타입의 삭제되지 않은 전체 엔티티 수
     */
    long countNonDel();

    /**
     * 고유 ID로 특정 엔티티를 물리적으로 삭제합니다.
     *
     * @param id 삭제할 엔티티의 ID
     */
    void deleteById(ID id);

    /**
     * 주어진 여러 ID에 해당하는 모든 엔티티를 물리적으로 삭제합니다.
     *
     * @param ids 삭제할 엔티티 ID들의 Iterable
     */
    void deleteAllById(Iterable<ID> ids);

    /**
     * 모든 엔티티를 물리적으로 삭제합니다.
     */
    void deleteAll();

    /**
     * 고유 ID로 특정 엔티티를 논리적으로 삭제(Soft Delete)합니다.
     *
     * @param id 논리적으로 삭제할 엔티티의 ID
     *
     *   **실제 데이터는 남아있습니다.**
     */
    void softDeleteById(ID id);

    /**
     * 모든 엔티티를 논리적으로 삭제합니다.
     *
     *  **실제 데이터는 남아있습니다.**
     */
    void softDeleteAll();
}