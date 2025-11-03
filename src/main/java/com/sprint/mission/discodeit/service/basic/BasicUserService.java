package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdatePasswordRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateType;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    public void create(CreateUserRequestDto request) {
        // 1. username/email 중복 검사
        if(userRepository.existsByNickName(request.getNickName())){
            throw new IllegalArgumentException("닉네임이 이미 존재합니다.");
        } else if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이메일이 이미 존재합니다.");
        }

        // 2. 선택적 프로필 이미지 처리
        UUID profileImageId = null;
        MultipartFile profileImageFile = request.getProfileImage();

        if(profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                byte[] imageBytes = request.getProfileImage().getBytes();
                BinaryContent profileImage = new BinaryContent(imageBytes);
                binaryContentRepository.save(profileImage);
                profileImageId = profileImage.getId();
            } catch (IOException e){
                throw new RuntimeException("프로필 이미지 업로드 실패", e);
            }
        }

        User newUser  = new User(
                request.getUserName(),
                request.getNickName(),
                request.getEmail(),
                request.getPhoneNum(),
                request.getLoginId(),
                request.getPassword(),
                profileImageId
        );

        userStatusRepository.save(new UserStatus(newUser.getId()));
        userRepository.save(newUser);
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
    public void update(UpdateUserRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        if (request.getType() == UpdateType.USER_NAME){
            user.setUserName(request.getUpdateParam());
        } else if(request.getType() == UpdateType.NICK_NAME) {
            userRepository.existsByNickName(request.getUpdateParam()); // 닉네임 중복 확인
            user.setNickName(request.getUpdateParam());
        } else if (request.getType() == UpdateType.EMAIL) {
            userRepository.existsByEmail(request.getUpdateParam()); // 이메일 중복 확인
            user.setEmail(request.getUpdateParam());
        } else if (request.getType() == UpdateType.PHONE_NUM){
            user.setPhoneNum(request.getUpdateParam());
        } else if (request.getType() == UpdateType.PROFILE) {
            MultipartFile profileImageFile = request.getProfileImage();

            if (profileImageFile != null && !profileImageFile.isEmpty()) {
                try {
                    BinaryContent binaryContent = new BinaryContent(profileImageFile.getBytes());
                    binaryContentRepository.save(binaryContent);
                    binaryContentRepository.delete(user.getProfileId()); // 기존 프로필 이미지 삭제
                    user.setProfileId(binaryContent.getId());
                } catch (IOException e) {
                    throw new RuntimeException("프로필 이미지 업로드 실패", e);
                }
            }
        }

        userRepository.update(user);
    }

    @Override
    public void updatePassword(UpdatePasswordRequestDto request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.setPassword(request.getNewPassword());
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
