package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.FriendRequest;
import com.sprint.mission.discodeit.entity.FriendUser;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MemoryFriendRequestRepository;


import java.util.List;
import java.util.UUID;

public class JCFFriendRequestService {

    private final MemoryFriendRequestRepository friendRequestRepository;
    private final JCFUserService userService;

    public JCFFriendRequestService(MemoryFriendRequestRepository friendRequestRepository, JCFUserService userService){
        this.friendRequestRepository=friendRequestRepository;
        this.userService=userService;
    }

    public void sendFriendRequest(User fromUser, UUID toUserId){
        User toUser = userService.getUser(toUserId);
        if(toUser==null){
            System.out.println("친구 추가하려는 유저가 존재하지 않습니다.");
            return;
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderId(fromUser.getId());
        friendRequest.setReceiverId(toUserId);
        friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequest> getReceivedFriendRequests(User user){
        UUID id = user.getId();
        return friendRequestRepository.findById(id);
    }

    public void acceptFriendRequest(FriendRequest request){
        UUID id = request.getReceiverId();
        friendRequestRepository.remove(id, request);
        User user1 = userService.getUser(request.getSenderId());
        User user2 = userService.getUser(request.getReceiverId());
        FriendUser friendUser1 = new FriendUser(user1);
        FriendUser friendUser2 = new FriendUser(user2);
        user1.getFriends().add(friendUser2);
        user2.getFriends().add(friendUser1);
    }
}
