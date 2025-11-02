package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    @Override
    public UserStatus createUserStatus(CreateUserStatusDto createUserStatusDto) {
        if(!userRepository.existsById(createUserStatusDto.getUserId())) {
            throw new NoSuchElementException("존재하지 않는 유저입니다." + createUserStatusDto.getUserId());
        }

        if(userStatusRepository.findByUserId(createUserStatusDto.getUserId()).isPresent()) {
            throw new NoSuchElementException("이미 존재하는 UserStatus입니다." + createUserStatusDto.getUserId());
        }

        UserStatus userstatus = new UserStatus(createUserStatusDto.getUserId(), createUserStatusDto.getLoginAt());
        userStatusRepository.save(userstatus);
        return userstatus;
    }

    @Override
    public UserStatus getUserStatus(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus입니다."));
    }

    @Override
    public List<UserStatus> getAllUserStatuses() {
        return userStatusRepository.findAll();
    }

    @Override
    public void updateUserStatus(UpdateUserStatusDto updateUserStatusDto) {
        UserStatus userStatus = userStatusRepository.findById(updateUserStatusDto.getUserStatusId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus입니다." + updateUserStatusDto.getUserStatusId()));

        userStatus.update(updateUserStatusDto.getLoginAt());
        userStatusRepository.save(userStatus);
    }

    @Override
    public void deleteById(UUID userStatusId) {
        if(!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException("찾을 수 없는 UserStauts입니다." + userStatusId);
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
