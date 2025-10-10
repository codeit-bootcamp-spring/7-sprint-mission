package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.FriendShip;
import com.sprint.mission.discodeit.repository.FriendShipRepository;
import com.sprint.mission.discodeit.service.FriendShipService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFFriendShipService implements FriendShipService {

    private final FriendShipRepository friendShipRepository;

    public JCFFriendShipService(FriendShipRepository friendShipRepository) {
        this.friendShipRepository = friendShipRepository;
    }

    @Override
    public void save(FriendShip entity) {
        friendShipRepository.save(entity);
    }

    @Override
    public void remove(FriendShip entity) {
        friendShipRepository.remove(entity);
    }

    @Override
    public FriendShip findById(UUID id) {
        return friendShipRepository.findById(id).orElseThrow(()->new NoSuchElementException("데이터가 없습니다"));
    }

    @Override
    public List<FriendShip> findAll() {
        return friendShipRepository.findAll();
    }
}
