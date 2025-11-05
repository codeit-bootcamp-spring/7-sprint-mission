package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.enums.Status;
import com.sprint.mission.discodeit.config.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserStatusServiceImpl extends BaseServiceImpl<UserStatus, UUID, UserStatusRepository> implements UserStatusService {

    public UserStatusServiceImpl(UserStatusRepository repository) {
        super(repository);
    }

    @Override
    public UserStatusDTO create(UUID userId) {
        return UserStatusDTO.fromEntity(save(UserStatus.create(userId)));
    }

    @Override
    public UserStatusDTO toAway(UUID userId) {
        UserStatus status = findByUserIdHelper(userId);
        status.setAway();
        return UserStatusDTO.fromEntity(save(status));
    }

    @Override
    public UserStatusDTO toOffline(UUID userId) {
        UserStatus status = findByUserIdHelper(userId);
        status.setOffline();
        return UserStatusDTO.fromEntity(save(status));
    }

    @Override
    public UserStatusDTO toOnline(UUID userId) {
        UserStatus status = findByUserIdHelper(userId);
        status.setOnline();
        return UserStatusDTO.fromEntity(save(status));
    }

    @Override
    public UserStatusDTO toDoNotDisturb(UUID userId, String message) {
        UserStatus status = findByUserIdHelper(userId);
        status.setDoNotDisturb(message);
        return UserStatusDTO.fromEntity(save(status));
    }

    @Override
    public UserStatusDTO findByUserId(UUID userId) {
        return UserStatusDTO.fromEntity(findByUserIdHelper(userId));
    }

    private UserStatus findByUserIdHelper(UUID userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자의 상태 정보를 가져올 수 없습니다."));
    }

    @Override
    public void deleteByUserId(UUID userId) {
        repository.deleteById(findByUserIdHelper(userId).getId());
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return repository.existsByUserId(userId);
    }

    @Override
    public List<UserStatusDTO> findAllByState(Status currentStatus) {
        return repository.findAllByState(currentStatus).stream()
                .map(UserStatusDTO::fromEntity)
                .toList();
    }
}
