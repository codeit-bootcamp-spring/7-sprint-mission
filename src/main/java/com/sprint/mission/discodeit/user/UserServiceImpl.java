package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.participation.dto.ParticipationRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserProfileUpdateDTO;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UUID, UserRepository> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserRequestDTO requestDTO) {
        String username = requestDTO.nickname();
        String password = passwordEncoder.encode(requestDTO.password());
        String email = requestDTO.email();
        String nickname = requestDTO.nickname();
        String phoneNum = requestDTO.phoneNum();
        // 1. '삭제된 기록을 포함하여' 같은 이름의 사용자가 있는지 Repository에서 직접 조회합니다.
        Optional<User> existingUser = repository.findByUsername(username);

        if(nicknameDuplicateCheck(nickname)){
            throw new IllegalArgumentException(nickname+"은(는) 이미 존재하는 닉네임입니다.");
        }

        if (repository.existsByUsername(username)) {
            // 2a. 기존 사용자가 있는 경우
            User user = existingUser.get();
            if (user.isDeleted()) {
                // 탈퇴한 사용자라면, 정보를 복원하고 업데이트합니다.
                user.restore();
                user.changePassword(password); // 비밀번호는 새로 설정
                user.updateProfile(nickname, email, phoneNum); // 프로필 정보 업데이트
                return save(user);
            } else {
                // 이미 활성 상태인 사용자라면, 예외를 발생시킵니다.
                throw new IllegalStateException("이미 존재하는 사용자 이름입니다: " + username);
            }
        } else {
            // 2b. 기존 사용자가 전혀 없는 경우 (신규 가입)
            return User.createUser(username, password, email, nickname, phoneNum);
        }
    }

    @Override
    public UserResponseDTO updateProfile(UUID userId, UserProfileUpdateDTO requestDTO) {
        String nickname = requestDTO.nickname();
        String email = requestDTO.email();
        String phoneNum = requestDTO.phoneNum();

        if(nicknameDuplicateCheck(nickname)){
            throw new IllegalArgumentException(nickname+"은(는) 이미 존재하는 닉네임입니다.");
        }

        User user = findByIdNonDel(userId);

        user.updateProfile(nickname, email, phoneNum);
        return UserResponseDTO.fromEntity(save(user));
    }

    @Override
    public void changePassword(UUID userId, String newPassword) {
        User user = findByIdNonDel(userId);
        user.changePassword(newPassword);
        save(user);
    }

    @Override
    public UserResponseDTO findByUsername(String username) {
        // Repository의 조회 결과를 처리하여, 없으면 비즈니스 규칙에 따라 예외를 던집니다.
        return UserResponseDTO.fromEntity(
                repository.findByUsername(username).orElseThrow(()
                        -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + username)));
    }



    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
    @Override
    public UserResponseDTO findByUsernameNonDel(String username) {
        return UserResponseDTO.fromEntity(
                repository.findByUsernameNonDel(username).orElseThrow(()
                -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + username)));
    }

    @Override
    public boolean existsByUsernameNonDel(String username) {
        return repository.existsByUsernameNonDel(username);
    }


    private boolean nicknameDuplicateCheck(String nickname) {
        return repository.existsByUserNickName(nickname);
    }
}