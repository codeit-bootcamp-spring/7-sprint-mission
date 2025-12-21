package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.Dto_UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserStatusException;
import com.sprint.mission.discodeit.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.mapper.dto.UserStatusDto;
import com.sprint.mission.discodeit.repository.jpa.UserStatusesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceUserStatusService;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class UserStatusService implements InterfaceUserStatusService {
    private final UserStatusesRepository userStatusRepository;
    private final UsersRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    public UserStatusDto create(UUID userId) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    [ ] 관련된 User가 존재하지 않으면 예외를 발생시킵니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Optional<UserStatus> userStatusOptional = userStatusRepository.findUserStatusByUserId(user.getId());

//     [ ] 같은 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
        if (userStatusOptional.isPresent()) {
            throw new UserStatusException(ErrorCode.DUPLICATE_USER, Map.of("userId", userId));
        }
        else {
            UserStatus userStatus = userStatusRepository.save(userStatusOptional.get());

            log.info("✅ UserStatusService.create = [" + user.getUsername() + "]");
            return userStatusMapper.toDto(userStatus);
        }
    }

    @Transactional(readOnly = true)
    public UserStatusDto find(UUID statusID) {
//      [ ] id로 조회합니다.
        UserStatus userStatus = userStatusRepository.findById(statusID)
                .orElseThrow(() -> new UserStatusNotFoundException(statusID));

        userRepository.findUserByUsername(userStatus.getUser().getUsername())
            .ifPresent(user -> log.info("✅ UserStatusService.find = [" + user.getUsername() + "]"));

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional(readOnly = true)
    public List<UserStatusDto> findAll() {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        List<UserStatusDto> userStatusDtoList = userStatusRepository
            .findAll()
            .stream()
            .map(userStatusMapper::toDto)
            .peek(userStatusDto -> log.info("✅ UserStatusService.findAll"))
            .toList();

        return userStatusDtoList;
    }

    @Transactional
    public void update(Dto_UserStatus dto) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        UserStatus userStatus = userStatusRepository.findById(dto.userStatusId())
                .orElseThrow(() -> new UserStatusNotFoundException(dto.userStatusId()));

        userStatus.setLastActiveAt(Instant.now());
        userStatusRepository.save(userStatus);

        User user = userRepository.findById(userStatus.getUser().getId())
            .orElseThrow(() -> new UserNotFoundException(userStatus.getUser().getId()));
        String userName = user.getUsername();
        log.info("✅ UserStatusService.update = [" + userName + "]");
    }

    @Transactional
    public UserStatusDto updateUserStatus(UUID userId, Instant newLastActiveAt) {
        UserStatus userStatus = userStatusRepository.findUserStatusByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        // 마지막 활동 시간 업데이트
        userStatus.setLastActiveAt(newLastActiveAt);
        userStatusRepository.save(userStatus);

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    public void delete(UUID statusID) {
//      [ ] id로 삭제합니다.
        userStatusRepository.deleteById(statusID);
        log.info("✅ UserStatusService.delete = [" + statusID.toString() + "]");
    }
}
