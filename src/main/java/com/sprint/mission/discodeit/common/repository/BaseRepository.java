package com.sprint.mission.discodeit.common.repository;

import com.sprint.mission.discodeit.common.utils.Deletable;
import com.sprint.mission.discodeit.common.utils.Identifiable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 모든 Repository가 공통적으로 가져야 할 기본 CRUD 기능을 정의하는 인터페이스입니다.
 *
 * @param <T>  리포지토리가 다룰 엔티티 타입 (Identifiable & Deletable을 구현해야 함)
 * @param <ID> 해당 엔티티의 ID 타입
 */
public interface BaseRepository<T extends Identifiable<ID> & Deletable, ID> {

    /**
     * 주어진 엔티티를 저장하거나 수정합니다.
     *
     * @param entity 저장 또는 수정할 엔티티
     */
    void save(T entity);

    /**
     * 주어진 여러 엔티티를 모두 저장하거나 수정합니다.
     *
     * @param entities 저장 또는 수정할 엔티티들의 컬렉션
     */
    void saveAll(Iterable<T> entities);

    /**
     * 고유 ID로 특정 엔티티를 조회합니다. (논리적 삭제 여부와 관계없이 조회)
     *
     * @param id 조회할 엔티티의 ID
     * @return 엔티티가 존재하면 Optional<T>로 감싸서 반환하고, 없으면 Optional.empty()를 반환합니다.
     */
    Optional<T> findById(ID id);

    /**
     * 고유 ID로 논리적으로 삭제되지 않은 특정 엔티티를 조회합니다.
     *
     * @param id 조회할 엔티티의 ID
     * @return 엔티티가 존재하고 삭제되지 않았으면 Optional<T>로, 없거나 삭제되었으면 Optional.empty()를 반환합니다.
     */
    Optional<T> findByIdNonDel(ID id);

    /**
     * 저장소의 모든 엔티티를 조회합니다. (논리적으로 삭제된 엔티티 포함)
     *
     * @return 모든 엔티티를 담은 List
     */
    List<T> findAll();

    /**
     * 논리적으로 삭제되지 않은 모든 엔티티를 조회합니다.
     *
     * @return 삭제되지 않은 엔티티를 담은 List
     */
    List<T> findAllNonDel();

    /**
     * 주어진 여러 ID에 해당하는 모든 엔티티를 조회합니다.
     *
     * @param ids 조회할 엔티티 ID들의 컬렉션
     * @return 조회된 엔티티들의 List
     */
    List<T> findAllById(Iterable<ID> ids);

    /**
     * 주어진 여러 ID에 해당하는 엔티티 중, 논리적으로 삭제되지 않은 엔티티만 조회합니다.
     *
     * @param ids 조회할 엔티티 ID들의 컬렉션
     * @return 조회된 엔티티들 중 삭제되지 않은 엔티티의 List
     */
    List<T> findAllByIdNonDel(Iterable<ID> ids);

    /**
     * 전체 엔티티의 개수를 반환합니다. (논리적으로 삭제된 엔티티 포함)
     *
     * @return long 타입의 전체 엔티티 수
     */
    long count();

    /**
     * 논리적으로 삭제되지 않은 전체 엔티티의 개수를 반환합니다.
     *
     * @return long 타입의 삭제되지 않은 엔티티 수
     */
    long countNonDel();

    /**
     * 주어진 ID를 가진 엔티티가 존재하는지 확인합니다. (논리적 삭제 여부와 관계없이 확인)
     *
     * @param id 확인할 엔티티의 ID
     * @return 존재하면 true, 아니면 false
     */
    boolean existsById(ID id);

    /**
     * 주어진 ID를 가진, 논리적으로 삭제되지 않은 엔티티가 존재하는지 확인합니다.
     *
     * @param id 확인할 엔티티의 ID
     * @return 존재하며 삭제되지 않았으면 true, 아니면 false
     */
    boolean existsByIdNonDel(ID id);

    /**
     * 고유 ID로 특정 엔티티를 물리적으로 삭제합니다.
     *
     * @param id 삭제할 엔티티의 ID
     */
    void deleteById(ID id);

    /**
     * 모든 엔티티를 물리적으로 삭제합니다.
     */
    void deleteAll();

    /**
     * 주어진 여러 ID에 해당하는 모든 엔티티를 물리적으로 삭제합니다.
     *
     * @param ids 삭제할 엔티티 ID들의 컬렉션
     */
    void deleteAllById(Iterable<ID> ids);

    /**
     * 논리적으로 삭제된 모든 엔티티를 물리적으로 삭제합니다.
     * (Soft Delete된 데이터를 영구적으로 제거합니다)
     */
    void deleteAllByIsDel();

    /**
     * 외부에서 생성된 데이터 맵을 주입받아 현재 데이터 맵을 교체합니다. (데이터 로딩용)
     * @param dataMap 로드된 데이터 맵
     */
    void loadDataMap(Map<ID, T> dataMap);

    /**
     * 내부의 데이터 맵을 외부로 노출합니다. (데이터 저장용)
     * @return 현재 데이터 맵
     */
    Map<ID, T> getDataMap();
}