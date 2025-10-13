package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * UserService 인터페이스
 * - 사용자 생성, 조회, 업데이트, 삭제 기능 제공
 * - 내부적으로 MessageService와 연동 가능
 */
public interface UserService {

    /**
     * 새로운 사용자 생성
     * @param userName 사용자 실명
     * @param nickName 사용자 닉네임
     * @param email 사용자 이메일
     * @param phoneNum 사용자 전화번호
     * @param userId 사용자 아이디
     * @param password 사용자 비밀번호
     */
    void createUser(String userName, String nickName, String email, String phoneNum, String userId, String password);

    /**
     * 이메일로 사용자 조회
     */
    User getUserByEmail(String email);

    /**
     * 전화번호로 사용자 조회
     */
    User getUserByPhone(String phoneNum);

    /**
     * 사용자ID로 사용자 조회
     */
    User getUserByUserId(String userId);

    /**
     * 전체 사용자 목록 조회
     */
    List<User> getAllUsers();

    /**
     * 로그인
     * @param userId 사용자ID
     * @param password 비밀번호
     * @return 로그인 성공 시 User, 실패 시 null
     */
    User login(String userId, String password);

    /**
     * UUID로 사용자 닉네임 조회
     */
    String getUserNickName(UUID id);

    /**
     * 사용자 정보 업데이트
     */
    void updateUser(User user);

    /**
     * 사용자 삭제
     * @param id 삭제할 User UUID
     */
    void deleteUser(UUID id);

    /**
     * @param channelId 삭제할 채널 UUID
     * @param user 대상 사용자
     */
    void deleteChannelFromUser(UUID channelId, User user);
}
