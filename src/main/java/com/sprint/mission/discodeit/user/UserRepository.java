package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.common.repository.BaseRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * User 엔티티의 영속성을 처리하기 위한 Repository 인터페이스입니다.
 * 공통 CRUD 기능은 BaseRepository로부터 상속받고, User 고유의 조회 기능을 추가로 정의합니다.
 */
public interface UserRepository extends BaseRepository<User, UUID> {

    /**
     * 사용자 이름(username)으로 사용자를 조회합니다.
     *
     * @param username 조회할 사용자의 이름
     * @return 사용자가 존재하면 Optional<User>로 감싸서 반환하고, 없으면 Optional.empty()를 반환합니다.
     */
    Optional<User> findByUsername(String username);
    /**
     * 해당 사용자 이름(username)이 이미 존재하는지 확인합니다.
     *
     * @param username 확인할 사용자의 이름
     * @return 사용자 이름이 존재하면 true, 아니면 false
     */
    boolean existsByUsername(String username);

    /**
     * 사용자 이름(username)으로 논리적으로 삭제되지 않은 사용자를 조회합니다.
     *
     * @param username 조회할 사용자의 이름
     * @return 사용자가 존재하고 삭제되지 않았으면 Optional<User>로, 없거나 삭제되었으면 Optional.empty()를 반환합니다.
     */
    Optional<User> findByUsernameNonDel(String username);

    /**
     * 해당 사용자 이름(username)을 가진, 논리적으로 삭제되지 않은 사용자가 존재하는지 확인합니다.
     *
     * @param username 확인할 사용자의 이름
     * @return 사용자 이름이 존재하며 삭제되지 않았으면 true, 아니면 false
     */
    boolean existsByUsernameNonDel(String username);

}