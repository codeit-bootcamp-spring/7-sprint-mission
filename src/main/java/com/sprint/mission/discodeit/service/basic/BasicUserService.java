package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserDto;
import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    //컬렉션 필드  final  메서드들은 이 데이터를 가지고 만들꺼다
    //왜 리스트였냐 수업중에 조회는,수정은 리스트
    // 추가(중간정도) , 삭제가 빈번하면 링크라들었다
    //마지막에 컴퓨터 좋아지면서 리스트를 많이쓴다 해서 리스트했다
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryRepository binaryRepository;


    @Override
    @Transactional
    public User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        boolean existEmail = userRepository.existsByEmail(userCreateRequest.email());
        boolean existName = userRepository.existsByUsername(userCreateRequest.username());

        //이메일매칭
        if (existEmail) {
            throw new IllegalArgumentException("이메일이 이미 존재합니다" + userCreateRequest.email());
        }
        //닉네임매칭
        if (existName) {
            throw new IllegalArgumentException("이름 이미 존재합니다" + userCreateRequest.username());
        }

        BinaryContent binaryContent = makeBinaryContent(optionalProfileCreateRequest);
        //유저정보
        User user = new User(
                userCreateRequest.username(),
                userCreateRequest.email(),
                userCreateRequest.password(),
                binaryContent
        );

        //유저정보저장

        UserStatus userStatus = new UserStatus(user, Instant.now());
        user.setStatus(userStatus);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto find(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("유저아이디로 찾을수없어"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("맞는 유저 ID가 없습니다 " + userId));


        boolean emailMatch = userRepository.existsByEmailAndIdNot(userUpdateRequest.newEmail(), userId);
        boolean nameMatch = userRepository.existsByUsernameAndIdNot(userUpdateRequest.newUsername(), userId);
        //이메일매칭
        if (emailMatch) {
            throw new IllegalArgumentException("이메일이 존재합니다 : " + userUpdateRequest.newEmail());
        }
        //네임매칭
        if (nameMatch) {
            throw new IllegalArgumentException("이름이 존재합니다 : " + userUpdateRequest.newUsername());
        }

        BinaryContent binaryContent = makeBinaryContent(optionalProfileCreateRequest);

        user.update(userUpdateRequest.newUsername(),
                userUpdateRequest.newEmail(),
                userUpdateRequest.newPassword(),
                binaryContent);

        return user;
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유유아이디없어요" + userId + " 이걸 못찾았어요"));

        userRepository.delete(user);
    }


    private BinaryContent makeBinaryContent(Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        return binaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                            contentType, bytes);
                    return binaryRepository.save(binaryContent);
                })
                .orElse(null);
    }


    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile().getId(),
                online
        );
    }
}
