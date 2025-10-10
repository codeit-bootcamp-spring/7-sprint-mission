package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.FriendRequest;
import com.sprint.mission.discodeit.repository.FriendRequestRepository;
import com.sprint.mission.discodeit.service.FriendRequestService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFFriendRequestService implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public JCFFriendRequestService(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public void save(FriendRequest entity) {
        friendRequestRepository.save(entity);
    }

    @Override
    public void remove(FriendRequest entity) {
        friendRequestRepository.remove(entity);
    }

    @Override
    public FriendRequest findById(UUID id) {
        return friendRequestRepository.findById(id).orElseThrow(()->new NoSuchElementException("데이터가 없습니다."));
    }

    @Override
    public List<FriendRequest> findAll() {
        return friendRequestRepository.findAll();
    }






}
