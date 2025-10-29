package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
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
    public UserStatusResponse create(UserStatusCreateRequest request) {
        Optional<User> repoUser = userRepository.findById(request.userId());
        Optional<UserStatus> statusUser = userStatusRepository.findByUserId(request.userId());
        //관련된 user가 존재하지 않아?
        if (!(repoUser.isPresent())) {
            throw new IllegalStateException("받은 uuid에 해당하는 유저가 없어 : " + request.userId());
        }
        //이미 관련 객체가 있어?
        if(statusUser.isPresent()){
            throw new IllegalStateException("이미 생성된 Userstatus가 있어 UUID 는 : " +statusUser.get().getUserId() );
        }

        UserStatus userStatus = userStatusRepository.save(request.userId());

        return UserStatusResponse.from(userStatus);
    }

    @Override
    public UserStatusResponse find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository
                .findByUserId(userStatusId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userStatusId));

         return UserStatusResponse.from(userStatus);
    }

    @Override
    public List<UserStatusResponse> findAll() {
        return userStatusRepository.findAll().stream().map(UserStatusResponse::from).toList();

    }

    @Override
    public UserStatusResponse update(UserStatustUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(request.userStatustId()).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + request.userStatustId()));
        userStatus.setUpdatedAt(Instant.now());
        return UserStatusResponse.from(userStatus);
    }

    @Override
    public UserStatusResponse updateByUserId(UUID userId) {
        UserStatus byUserId = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userId));
        byUserId.setUpdatedAt(Instant.now());
        return UserStatusResponse.from(byUserId);
    }

    @Override
    public void delete(UUID userStatusId) {
      userStatusRepository.deleteByUserId(userStatusId);
    }
}
