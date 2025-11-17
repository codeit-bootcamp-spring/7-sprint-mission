package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.user.dto.UserProfileUpdateDTO;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;

import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import java.util.UUID;

/**
 * 사용자(User) 도메인의 비즈니스 로직을 처리하는 서비스 인터페이스입니다. 공통 CRUD 기능은 BaseService로부터 상속받습니다.
 */
public interface UserService extends BaseService<User, UUID> {

  User createUser(UserRequestDTO requestDTO);

  UserResponseDTO updateProfile(UUID userId, UserProfileUpdateDTO requestDTO);

  UserResponseDTO changeUserName(UUID userId, String newUserName);

  /**
   * 사용자의 비밀번호를 변경합니다.
   *
   * @param userId      비밀번호를 변경할 사용자의 고유 ID
   * @param newPassword 새로운 비밀번호
   */
  void changePassword(UUID userId, String newPassword);

  /**
   * 사용자 이름(username)으로 특정 사용자를 조회합니다.
   *
   * @param username 조회할 사용자의 이름
   * @return 조회된 User 객체
   * @throws java.util.NoSuchElementException 해당 이름의 사용자가 없을 경우
   */
  UserResponseDTO findByUsername(String username);

  /**
   * 특정 사용자 이름이 이미 존재하는지 확인합니다.
   *
   * @param username 확인할 사용자 이름
   * @return 존재하면 true, 존재하지 않으면 false
   */
  boolean existsByUsername(String username);


  UserResponseDTO findByUsernameNonDel(String username);

  boolean existsByUsernameNonDel(String username);

  UserResponseDTO tempUpdateProfile(UUID userId, UserUpdateRequest request);
}