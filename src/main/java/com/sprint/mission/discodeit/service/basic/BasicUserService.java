package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupCommand;
import com.sprint.mission.discodeit.dto.user.UserUpdateCommand;
import com.sprint.mission.discodeit.dto.user.UserUpdateParams;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserDuplicateException;
import com.sprint.mission.discodeit.mapper.UserMapperManual;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserReader userReader;
    private final BinaryContentService binaryContentService;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapperManual userMapper;


    @Override
    @Transactional
    public UserResponseDto signUp(UserSignupCommand userSignupCommand) {
        if (
                userSignupCommand.username() == null ||
                        userSignupCommand.username().isBlank() ||
                        userSignupCommand.password() == null ||
                        userSignupCommand.password().isBlank() ||
                        userSignupCommand.email() == null || userSignupCommand.email().isBlank()

        ) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("회원가입 처리 시작 - hasProfile={}", userSignupCommand.profile().isPresent());

        if (userRepository.existsByEmail(userSignupCommand.email()) || userRepository.existsByUsername(userSignupCommand.username())) {
            throw new UserDuplicateException();
        }

        UUID profileBinaryId = userSignupCommand.profile().map(binaryContentService::uploadBinaryContent).orElse(null); // NOTE: 서비스 의존은 지양해야하지만, 순환참조 없고 해당 서비스가 다른 서비스에 의존하는게 아니면 공통된건 사용해도 좋고, 오히려 Service라는 이름보단 -Uploader @Component로 구성하는게 나을수도있다.
        BinaryContent binaryContentReference = (profileBinaryId != null)
                ? binaryContentRepository.getReferenceById(profileBinaryId) // NOTE: 위의 update 반환값으로 BinaryId를 받고 실제 엔티티 DB조회 필요없이 id만 가진 프록시 객체로 활용하기위해 유지
                : null;

        User newUser = User.create(userSignupCommand.username(),
                userSignupCommand.email(),
                userSignupCommand.password(),
                binaryContentReference
        );

        // NOTE: user객체 생성후 userStatus도 넣어서 cascade 영향으로 같이 insert되도록
        newUser.initUserStatus();
        User savedUser = userRepository.save(newUser);

        log.info("회원가입 완료 - userId={}, hasProfile={}", savedUser.getId(), profileBinaryId != null);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID userId) {

        User user = userReader.findUserOrThrow(userId);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        if (userId == null) { // NOTE: 서비스 레이어 public API라 컨트롤러 외 테스트, 배치, 이벤트 핸들러에서 요청 가능하므로 최소 필수 가드로 남김
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("회원 삭제 처리 시작 - userId={}", userId);
        User user = userReader.findUserOrThrow(userId);
        userRepository.deleteById(user.getId());
        log.info("회원 삭제 완료 - userId={}", userId);

    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserUpdateCommand updateCommand) {
        if (updateCommand.id() == null) { // NOTE: update 는 부분 변경이므로 userId만 가드, 나머지는 Null 허용으로 미변경 정책으로 봄
            // NOTE: 서비스 레이어 public API라 컨트롤러 외 테스트, 배치, 이벤트 핸들러에서 요청 가능하므로 최소 필수 가드로 남김
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("회원 수정 처리 시작 - userId={}, hasProfile={}",
                updateCommand.id(),
                updateCommand.profile().isPresent()
        );

        User userById = userReader.findUserOrThrow(updateCommand.id());

        UUID profileBinaryId = updateCommand.profile().map(binaryContentService::uploadBinaryContent).orElse(null);

        BinaryContent binaryContentReference = (profileBinaryId != null)
                ? binaryContentRepository.getReferenceById(profileBinaryId) // NOTE: 위의 update 반환값으로 BinaryId를 받고 실제 엔티티 DB조회 필요없이 id만 가진 프록시 객체로 활용하기위해 유지
                : null;

        UserUpdateParams params = UserUpdateParams.from(updateCommand, binaryContentReference); // 경계분리
        userById.update(params);
        userRepository.save(userById);// user repository 사용 책임 분리

        log.info("회원 수정 완료 - userId={}, profileChanged={}", updateCommand.id(), profileBinaryId != null);

        return userMapper.toDto(userById); // NOTE: 멱등성, dirty checking 으로 바뀌던 안바뀌던 해당 객체 반환
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {

        List<User> all = userRepository.findAll();

        return all.stream()
                .map(userMapper::toDto)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByIds(List<UUID> userIds) {
        if (userIds == null) {  // NOTE: 서비스 레이어 public API라 컨트롤러 외 테스트, 배치, 이벤트 핸들러에서 요청 가능하므로 최소 필수 가드로 남김
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("유저 다중 조회 - size={}", userIds.size());

        return userRepository.findAllById(userIds);
    }
}
