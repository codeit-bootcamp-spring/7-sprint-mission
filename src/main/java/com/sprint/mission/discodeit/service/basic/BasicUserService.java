package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.vaildator.UserVaildator;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService{

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponseDto create(CreateUserRequestDto userRequest, MultipartFile file) {
        // 1. nickname/email 중복 검사
        if(userRepository.existsByNickName(userRequest.getNickName())){
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        } else if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        // 2. 입력된 정보 형태 검증
        Optional.ofNullable(userRequest.getNickName()).ifPresent(n -> UserVaildator.vaildateNickname(n));
        Optional.ofNullable(userRequest.getEmail()).ifPresent(e -> UserVaildator.vaildateEmail(e));
        Optional.ofNullable(userRequest.getPhoneNum()).ifPresent(pn -> UserVaildator.vaildatePhoneNum(pn));
        Optional.ofNullable(userRequest.getPassword()).ifPresent(pw -> UserVaildator.vaildatePassword(pw));

        // 3. 선택적 프로필 이미지 처리
        UUID profileImageId = null;

        if(file != null && !file.isEmpty()) {
            try {
                byte[] imageBytes = file.getBytes();
                BinaryContent profileImage = new BinaryContent(imageBytes);
                binaryContentRepository.save(profileImage);
                profileImageId = profileImage.getId();
            } catch (IOException e){
                throw new RuntimeException("프로필 이미지 업로드 실패", e);
            }
        }

        User newUser  = new User(
                userRequest.getUserName(),
                userRequest.getNickName(),
                userRequest.getEmail(),
                userRequest.getPhoneNum(),
                userRequest.getLoginId(),
                userRequest.getPassword(),
                profileImageId
        );

        userStatusRepository.save(new UserStatus(newUser.getId()));
        userRepository.save(newUser);

        return UserResponseDto.from(newUser, false);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));

        boolean active = userStatusRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("userstatus가 존재하지 않습니다."))
                .isActiveUser();

        return UserResponseDto.from(user, active);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));

        boolean active = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("userstatus가 존재하지 않습니다."))
                .isActiveUser();

        return UserResponseDto.from(user, active);
    }

    @Override
    public UserResponseDto findByPhoneNum(String phoneNum) {
        User user = userRepository.findByPhone(phoneNum)
                .orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));

        boolean active = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("userstatus가 존재하지 않습니다."))
                .isActiveUser();

        return UserResponseDto.from(user, active);
    }

    @Override
    public UserResponseDto findByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        boolean active = userStatusRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("userstatus가 존재하지 않습니다."))
                .isActiveUser();

        return UserResponseDto.from(user, active);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(u -> UserResponseDto.from(
                        u,
                        userStatusRepository.findById(u.getId())
                                .orElseThrow(() -> new IllegalStateException("userstatus가 존재하지 않습니다."))
                                .isActiveUser()))
                .collect(Collectors.toList());
    }

    @Override
    public String findNickNameById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."))
                .getNickName();
    }

    @Override
    public void update(UUID userId, UpdateUserRequestDto request, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        String userName = null;
        String nickName = null;
        String email = null;
        String phoneNum = null;
        String password = null;

        // 프로필 이미지만 전달된 경우 null 참조 방지
        if (Optional.ofNullable(request).isPresent()) {
            userName = request.getNewUserName();
            nickName = request.getNewNickName();
            email = request.getNewEmail();
            phoneNum = request.getNewPhoneNum();
            password = request.getNewPassword();

            // 닉네임 중복 확인
            if (userRepository.existsByNickName(nickName)) {
                throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
            }

            // 이메일 중복 확인
            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
            }

            // 입력된 정보 형태 검증
            Optional.ofNullable(nickName).ifPresent(n -> UserVaildator.vaildateNickname(n));
            Optional.ofNullable(email).ifPresent(e -> UserVaildator.vaildateEmail(e));
            Optional.ofNullable(phoneNum).ifPresent(pn -> UserVaildator.vaildatePhoneNum(pn));
            Optional.ofNullable(password).ifPresent(pw -> UserVaildator.vaildatePassword(pw));
        }

        UUID profileImageId = null;
        if (file != null && !file.isEmpty()) {
            try {
                byte[] imageBytes = file.getBytes();
                BinaryContent profileImage = new BinaryContent(imageBytes);
                binaryContentRepository.save(profileImage);
                profileImageId = profileImage.getId();
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지를 저장하는데 실패했습니다.");
            }
            binaryContentRepository.delete(user.getProfileId()); // 기존 프로필 이미지 삭제
        }

        user.update(userName, nickName, email, phoneNum, password, profileImageId);
        userRepository.update(user);
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        messageRepository.deleteByUser(id);
        userStatusRepository.deleteById(user.getId());
        binaryContentRepository.delete(user.getProfileId());
        userRepository.deleteById(id);
    }

    @Override
    public boolean isPasswordMatch(UUID userId, String password) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."))
                .getPassword().equals(password);
    }
}
