package com.sprint.mission.discodeit.user.application;


import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;
import com.sprint.mission.discodeit.friendrequest.infrastructure.FriendRequestRepository;
import com.sprint.mission.discodeit.friendship.domain.FriendShip;
import com.sprint.mission.discodeit.friendship.infrastructure.FriendShipRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.exception.DuplicateUserException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {


    private final UserRepository userRepository;
    private final FriendRequestRepository requestRepository;
    private final FriendShipRepository friendShipRepository;

    public BasicUserService(UserRepository userRepository, FriendRequestRepository requestRepository,
                            FriendShipRepository friendShipRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.friendShipRepository = friendShipRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
        System.out.println("유저 저장 성공");
    }

    @Override
    public void remove(User user) {
        findById(user.getId());
        userRepository.remove(user);
        System.out.println("유저 삭제 성공");
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    //이메일로 유저 정보 가져오기
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    @Override
    public FriendRequest sendFriendRequest(User sender, User target) {
        validateNotAlreadyFriend(sender, target);
        validateNotDuplicateRequest(sender, target);
        FriendRequest friendRequest = sender.sendFriendRequestTo(target);
        requestRepository.save(friendRequest);
        return friendRequest;

    }

    @Override
    public List<FriendRequest> getSentFriendRequests(User user) {
        return requestRepository.findAll().stream().filter(r -> r.getSenderId().equals(user.getId())).toList();
    }

    @Override
    public List<FriendRequest> getReceivedFriendRequests(User user) {
        return requestRepository.findAll().stream().filter(r -> r.getReceiverId().equals(user.getId())).toList();
    }

    @Override
    public void acceptFriendRequest(User receiver, FriendRequest request) {
        FriendShip friendShip = receiver.acceptFriendRequest(request);
        requestRepository.remove(request);
        friendShipRepository.save(friendShip);
    }

    public void register(User user) {
        validateNotDuplicateUser(user);
        save(user);
    }

    private void validateNotDuplicateUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateUserException("이미 존재하는 이메일입니다: " + user.getEmail());
        }
    }



    // 이미 친구인지 검사
    private void validateNotAlreadyFriend(User sender, User target) {
        boolean alreadyFriends =
                friendShipRepository.findAll().stream()
                        .anyMatch(fs ->
                                (fs.getUserAId().equals(sender.getId()) && fs.getUserBId().equals(target.getId())) ||
                                        (fs.getUserAId().equals(target.getId()) && fs.getUserBId().equals(sender.getId())));
        if (alreadyFriends) {
            throw new IllegalStateException("이미 친구 관계입니다");
        }
    }

    // 중복 요청 검사
    private void validateNotDuplicateRequest(User sender, User target) {
        boolean alreadySent = requestRepository.findAll().stream()
                .anyMatch(req -> req.getSenderId().equals(sender.getId()) &&

                        req.getReceiverId().equals(target.getId()));
        if (alreadySent) {
            throw new IllegalStateException("이미 친구 요청을 보냈습니다");
        }
    }





}
