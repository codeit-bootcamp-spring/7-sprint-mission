package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.PrintUtil;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_UserStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_UserStatusByID;
import com.sprint.mission.discodeit.entity.dto.Res_UserStatus;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.service.InterfaceUserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService implements InterfaceUserStatusService {
    private final FileUserStatusRepository userStatusRepository;
    private final FileUserRepository userRepository;

    public Res_UserStatus create(Dto_UserStatusByID dtoUserStatus) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    [ ] 관련된 User가 존재하지 않으면 예외를 발생시킵니다.
//    [ ] 같은 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
        User user = userRepository.findById(dtoUserStatus.userId())
                .orElseThrow(() -> new IllegalArgumentException(("Res_UserStatus.create.userId = [" + dtoUserStatus.userId() + "] err")));
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(user.getId());
        if (userStatus != null && userStatus.isPresent()) {
            userStatusRepository.save(userStatus.get());

            PrintUtil.okMessage("UserStatusService.create = [" + user.getUserName() + "]");
            return Res_UserStatus.from(userStatus.get());
        }
        else {
            throw new IllegalArgumentException("🚨Res_UserStatus.create.userId = [" + dtoUserStatus.userId() + "] err");
        }
    }

    public Res_UserStatus find(UUID statusID) {
//    [ ] id로 조회합니다.
        UserStatus userStatus = userStatusRepository.findById(statusID)
                .orElseThrow(() -> new IllegalArgumentException("🚨UserStatusService.find.statusID = [" + statusID + "] err"));

        Optional<User> user = userRepository.findById(userStatus.getUserId());
        PrintUtil.okMessage("UserStatusService.find = [" + user.get().getUserName() + "]");

        return Res_UserStatus.from(userStatus);
    }

    public List<Res_UserStatus> findAll() {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    수정 대상 객체의 id 파라미터, 수정할 값 파라미터
        List<UserStatus> userStatuses = userStatusRepository.findAll()
                .orElseThrow(() -> new IllegalArgumentException("🚨UserStatusService.findAll() err"));
        List<Res_UserStatus> list = userStatuses.stream().map(Res_UserStatus::from).toList();

        for (Res_UserStatus resUserStatus : list) {
            PrintUtil.okMessage("UserStatusService.findAll = [" + userRepository.findById(resUserStatus.userId()).get().getUserName() + "]");
        }

        return list;
    }

    public void update(Dto_UserStatus dto) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//    수정 대상 객체의 id 파라미터, 수정할 값 파라미터
        UserStatus userStatus = userStatusRepository.findById(dto.id())
                .orElseThrow(() -> new IllegalArgumentException("🚨UserStatusService.update.id = [" + dto.id() + "] err"));

        boolean isOnline = userStatus.isOnline();

        userStatus.setOnlineState(isOnline);
        userStatus.setUpdatedAt();
        userStatusRepository.save(userStatus);

        String userName = userRepository.findById(userStatus.getUserId()).get().getUserName();
        PrintUtil.okMessage("UserStatusService.update = [" + userName + "] isOnline = [" + isOnline + "]");
    }

    public void updateByUserID(Dto_UserStatusByID dto) {
//    [ ] userId 로 특정 User의 객체를 업데이트합니다.
        UserStatus userStatus = userStatusRepository.findByUserId(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("🚨UserStatusService.updateByUserID.userID = [" + dto.userId() + "] err"));

        boolean isOnline = userStatus.isOnline();

        userStatus.setOnlineState(isOnline);
        userStatus.setUpdatedAt();
        userStatusRepository.save(userStatus);
        PrintUtil.okMessage("UserStatusService.updateByUserID = [" + dto.userId() + "] isOnline = [" + isOnline + "]");
    }

    public void delete(UUID statusID) {
//    [ ] id로 삭제합니다.
        userStatusRepository.deleteById(statusID);
        PrintUtil.okMessage("UserStatusService.delete = [" + statusID + "]");
    }

}
