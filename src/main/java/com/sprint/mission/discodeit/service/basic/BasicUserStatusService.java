package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatustUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

    private  final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        Optional<User> repoUser = userRepository.findById(request.userId());
        Optional<UserStatus> statusUser = userStatusRepository.findByUserId(request.userId());
        //관련된 user가 존재하지 않아?
        if (repoUser.isEmpty()) {
            throw new IllegalStateException("받은 uuid에 해당하는 유저가 없어 : " + request.userId());
        }
        //이미 관련 객체가 있어?
        if(statusUser.isPresent()){
            throw new IllegalStateException("이미 생성된 Userstatus가 있어 UUID 는 : " +statusUser.get().getUserId() );
        }
        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = new UserStatus(request.userId(),lastActiveAt);

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {

        return userStatusRepository
                .findByUserId(userStatusId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userStatusId));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream()
                .toList();

    }


    @Override
    public UserStatus update(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();
        UserStatus userStatus = userStatusRepository.findByUserId(userId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userId));
        userStatus.update(newLastActiveAt);

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();
        UserStatus byUserId = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userId));
        byUserId.update(newLastActiveAt);

        return userStatusRepository.save(byUserId);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.findAll().stream().anyMatch(userStatus -> userStatus.getId().equals(userStatusId))) {
            throw new NoSuchElementException("유저스타터스아이디못찾아" + userStatusId + "못찾아 ");
        }

        userStatusRepository.deleteByUserId(userStatusId);
    }
}
