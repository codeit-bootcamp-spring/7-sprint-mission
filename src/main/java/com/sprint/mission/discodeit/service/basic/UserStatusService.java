package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_UserStatus;
import com.sprint.mission.discodeit.entity.dto.Res_UserStatus;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import com.sprint.mission.discodeit.repository.InterfaceUserStatusRepository;
import com.sprint.mission.discodeit.service.InterfaceUserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService implements InterfaceUserStatusService {
    private final InterfaceUserStatusRepository userStatusRepository;
    private final InterfaceUserRepository userRepository;

    public Res_UserStatus create(UUID userId) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    [ ] 관련된 User가 존재하지 않으면 예외를 발생시킵니다.
//    [ ] 같은 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(("🚨Res_UserStatus.create.id = [" + userId.toString() + "] err")));

        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(user.getId());

        if (userStatus != null && userStatus.isPresent()) {
            userStatusRepository.save(userStatus.get());

            log.info("✅ UserStatusService.create = [" + user.getUserName() + "]");
            return Res_UserStatus.from(userStatus.get());
        }
        else {
            throw new IllegalArgumentException("🚨Res_UserStatus.create.id = [" + userId.toString() + "] err");
        }
    }

    public Res_UserStatus find(UUID statusID) {
//      [ ] id로 조회합니다.
        UserStatus userStatus = userStatusRepository.findById(statusID)
                .orElseThrow(() -> new IllegalArgumentException("🚨UserStatusService.find.statusID = [" + statusID.toString() + "] err"));

        userRepository.findById(userStatus.getUserId())
            .ifPresent(user -> log.info("✅ UserStatusService.find = [" + user.getUserName() + "]"));

        return Res_UserStatus.from(userStatus);
    }

    public List<Res_UserStatus> findAll() {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        List<Res_UserStatus> list = userStatuses.stream().map(Res_UserStatus::from).toList();

        for (Res_UserStatus resUserStatus : list) {
            log.info("✅ UserStatusService.findAll = ["
                + userRepository.findById(resUserStatus.userId())
                                .get()
                                .getUserName()
                + "]");
        }

        return list;
    }

    public void update(Dto_UserStatus dto) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        UserStatus userStatus = userStatusRepository.findById(dto.userStatusId())
                .orElseThrow(() -> new IllegalArgumentException("🚨UserStatusService.update.userStatusId = [" + dto.userStatusId().toString() + "] err"));

//        boolean online = userStatus.online();
//        userStatus.setOnlineState(online);
        userStatus.setUpdatedAtNow();
        userStatusRepository.save(userStatus);

        User user = userRepository.findById(userStatus.getUserId())
            .orElseThrow(() -> new IllegalArgumentException(
                "🚨UserStatusService.update.userStatus = [" + userStatus.getUserId().toString() + "]"));
        String userName = user.getUserName();
        log.info("✅ UserStatusService.update = [" + userName + "]");
    }

    public Res_UserStatus updateUserStatus(UUID userId, Instant newLastActiveAt) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("🚨해당 User[" + userId.toString() + "]의 UserStatus를 찾을 수 없음"));

      // 마지막 활동 시간 업데이트
      userStatus.setNewLastActiveAt(newLastActiveAt);
      userStatusRepository.save(userStatus);

      log.info("✅ UserStatusService.userStatus = [" + userStatus + "]");
      return Res_UserStatus.from(userStatus);
    }

    public void delete(UUID statusID) {
//    [ ] id로 삭제합니다.
        userStatusRepository.deleteById(statusID);
        log.info("✅ UserStatusService.delete = [" + statusID.toString() + "]");
    }
}
