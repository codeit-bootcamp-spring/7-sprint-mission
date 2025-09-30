package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * 사용자(User)와 관련된 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 이 인터페이스는 사용자 생성, 조회, 수정, 삭제(CRUD) 및 기타 사용자 관련 기능을 정의합니다.
 */
public interface UserService {

    /**
     * 새로운 사용자를 생성하고 저장소에 저장합니다.
     *
     * @param username   사용자 이름 (필수, 고유해야 함)
     * @param password   비밀번호 (필수)
     * @param email      이메일 (필수)
     * @param nickname   닉네임 (선택)
     * @param phoneNum   전화번호 (선택)
     * @return 생성된 User 객체
     */
    User createUser(String username, String password, String email, String nickname, String phoneNum);

    /**
     * 특정 사용자의 프로필 정보(닉네임, 이메일, 전화번호)를 수정합니다.
     *
     * @param userId     수정할 사용자의 고유 ID
     * @param nickname   새로운 닉네임
     * @param email      새로운 이메일
     * @param phoneNum   새로운 전화번호
     * @return 수정이 완료된 User 객체
     */
    User updateProfile(UUID userId, String nickname, String email, String phoneNum);

    /**
     * 사용자의 상태를 '온라인'으로 변경합니다.
     *
     * @param userId 상태를 변경할 사용자의 고유 ID
     */
    void goOnline(UUID userId);

    /**
     * 사용자의 상태를 '오프라인'으로 변경합니다.
     *
     * @param userId 상태를 변경할 사용자의 고유 ID
     */
    void goOffline(UUID userId);

    /**
     * 사용자의 상태를 '자리 비움'으로 변경합니다.
     *
     * @param userId 상태를 변경할 사용자의 고유 ID
     */
    void setAway(UUID userId);

    /**
     * 사용자의 상태를 '방해 금지'로 변경합니다.
     *
     * @param userId 상태를 변경할 사용자의 고유 ID
     */
    void setDoNotDisturb(UUID userId);

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param userId      비밀번호를 변경할 사용자의 고유 ID
     * @param newPassword 새로운 비밀번호
     */
    void changePassword(UUID userId, String newPassword);

    /**
     * 고유 ID(UUID)를 사용하여 특정 사용자를 조회합니다.
     *
     * @param userId 조회할 사용자의 고유 ID
     * @return 조회된 User 객체
     * @throws java.util.NoSuchElementException 해당 ID의 사용자가 없을 경우
     */
    User findById(UUID userId);

    /**
     * 저장된 모든 사용자를 조회합니다.
     *
     * @return 모든 User 객체를 담은 List, 사용자가 없으면 빈 리스트를 반환
     */
    List<User> findAll();

    /**
     * 사용자 이름(username)을 사용하여 특정 사용자를 조회합니다.
     *
     * @param username 조회할 사용자의 이름
     * @return 조회된 User 객체
     * @throws java.util.NoSuchElementException 해당 이름의 사용자가 없을 경우
     */
    User findByUsername(String username);

    /**
     * 특정 사용자 이름이 이미 존재하는지 확인합니다.
     *
     * @param username 확인할 사용자 이름
     * @return 존재하면 true, 존재하지 않으면 false
     */
    boolean existsByUsername(String username);

    /**
     * 고유 ID를 사용하여 특정 사용자를 삭제합니다.
     *
     * @param userId 삭제할 사용자의 고유 ID
     */
    void deleteById(UUID userId);

    /**
     * 모든 사용자를 삭제합니다.
     */
    void deleteAll();

    /**
     * 전체 사용자 수를 반환합니다.
     *
     * @return long 타입의 전체 사용자 수
     */
    long count();

    /**
     * 특정 사용자가 '온라인' 상태인지 확인합니다.
     *
     * @param userId 확인할 사용자의 고유 ID
     * @return 온라인 상태이면 true, 아니면 false
     */
    boolean isOnline(UUID userId);

    /**
     *
     * @param userId 확인할 사용자의 고유 ID
     * @return 오프라인 상태이면 true, 아니면 false
     */
    boolean isOffline(UUID userId);

    /**
     * 특정 사용자가 '자리 비움' 상태인지 확인합니다.
     *
     * @param userId 확인할 사용자의 고유 ID
     * @return 자리 비움 상태이면 true, 아니면 false
     */
    boolean isAway(UUID userId);

    /**
     * 특정 사용자가 '방해 금지' 상태인지 확인합니다.
     *
     * @param userId 확인할 사용자의 고유 ID
     * @return 방해 금지 상태이면 true, 아니면 false
     */
    boolean isDoNotDisturb(UUID userId);
}