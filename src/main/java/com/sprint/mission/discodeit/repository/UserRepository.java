package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository
 * -----------------
 * 유저 데이터의 저장/조회/삭제를 담당하는 저장소 계층 인터페이스입니다.
 * (실제 데이터 저장소는 메모리, DB 등 다양하게 구현될 수 있습니다.)
 */
public interface UserRepository {

    /** 새로운 유저 저장 */
    void save(User user);

    /** ID(UUID)로 유저를 조회 */
    Optional<User> findById(UUID id);

    /** 이메일로 유저를 조회 */
    Optional<User> findByEmail(String email);

    /** 전화번호로 유저를 조회 */
    Optional<User> findByPhone(String phoneNum);

    /** 아이디로 유저를 조회 */
    Optional<User> findByUserId(String userId);

    /** 모든 유저를 반환 */
    List<User> findAll();

    /** 유저 정보 수정 */
    void update(User user);

    /** ID(UUID)로 유저를 삭제 */
    void deleteById(UUID id);

    /** ID(UUID)로 유저 존재 여부 확인*/
    boolean isExist(UUID id);

    boolean existsByNickName(String NickName);

    boolean existsByEmail(String email);

}
