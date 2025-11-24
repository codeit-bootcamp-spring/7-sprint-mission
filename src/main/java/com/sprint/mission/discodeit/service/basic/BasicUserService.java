package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.email.EmailSender;
import com.sprint.mission.discodeit.dto.auth.response.AvailabilityRes;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  //리포지토리
  private final UserRepository userRepository;
  private final EmailSender emailSender;

  // ===== 🎯 Controller Direct (DTO 반환) =====
  //메일로 가입했던 아이디를 발송
  public void sendEmailId(String email) {
    User user = findByEmail(email);
    emailSender.sendEmailAsync(
        email,
        "[ch-ah] 가입하신 아이디를 보내드립니다",
        "nickname: " + user.getNickname()
    );
  }

  //메일로 임시 비밀번호 발송 및 임시 비밀번호 발급
  public void sendEmailTemporaryPassword(String email, String nickname) {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
    );
    if (!user.getNickname().equals(nickname)) {
      throw new CustomException(ErrorCode.INVALID_USER_NICKNAME);
    }
    String passwordTemp = UUID.randomUUID().toString().replaceAll("-", "");
    userRepository.updatePasswordTemporary(user.getId(), passwordTemp);
    emailSender.sendEmailAsync(
        email,
        "[ch-at] 임시 비밀번호를 보내드립니다",
        "임시 비밀번호: " + passwordTemp + "\n 반드시 이후에 비밀번호 변경을 해주세요."
    );
  }

  // ===== 🏗️ Domain Logic (Facade 용)  =====
  //유저 추가
  @Override
  public User create(User user) {
    validateDuplicate(user.getEmail(), user.getNickname());
    return userRepository.save(user);
  }

  //유저 목록
  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  //유저 아이디로 조회
  @Override
  public User findById(UUID id) {
    return userRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
    );
  }

  //이메일 찾기
  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  //닉네임으로 찾기
  @Override
  public User findByNickname(String nickname) {
    return userRepository.findByNickname(nickname).orElse(null);
  }

  //삭제
  @Override
  public void delete(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    userRepository.delete(id);
  }

  //업데이트
  @Override
  public void update(UUID id, UserUpdateReq req) {
    String replacaPassword = req.password();
    User user = findById(id);
    if (req.password() == null) {
      replacaPassword = user.getPassword();
    }
    validateDuplicate(id, req.email(), req.nickname());
    userRepository.update(id, req.email(), req.nickname(), replacaPassword);
  }

  @Override
  public void updateProfileImage(UUID id, UUID profileId) {
    if (!userRepository.existsById(id)) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    userRepository.updateProfileImage(id, profileId);
  }

  //해당 닉네임으로 가입된 사림이 있는지.
  @Override
  public AvailabilityRes isRegisteredNickname(String nickname) {
    return new AvailabilityRes(
        userRepository.existsByNickname(nickname));
  }

  //해당 이메일로 가입된 사림이 있는지.
  @Override
  public AvailabilityRes isRegisteredEmail(String email) {
    return new AvailabilityRes(
        userRepository.existsByEmail(email));
  }


  // ===== 🔒 Private Logic (내부 사용) =====
  // 신규 유저 중복 검사
  private void validateDuplicate(String email, String nickname) {
    if (userRepository.existsByEmail(email)) {
      throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    if (userRepository.existsByNickname(nickname)) {
      throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
  }

  //기존 유저 회원 정보 수정 시 중복 검사
  private void validateDuplicate(UUID userId, String email, String nickname) {
    if (userRepository.existsByEmailAndIdNot(email, userId)) {
      throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    if (userRepository.existsByNicknameAndIdNot(nickname, userId)) {
      throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
  }
}
