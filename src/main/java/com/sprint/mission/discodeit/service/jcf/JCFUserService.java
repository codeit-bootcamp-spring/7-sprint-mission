package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.UserService;


import java.util.*;

public class JCFUserService implements UserService {

    //자바에서 final이 붙는 건 참조 자체를 변경할 수 없게 만든다는 뜻
    //그래서 리스트 안에 있는 내용은 수정 가능
    private final Map<UUID, User> users=new HashMap<>();

    public Map<UUID, User> getUsers() {
        return users;
    }

    @Override
    public void addUser(User user) {
        //nickName 제외 다른 모든 필드를 입력해야지만 User등록 가능
        if(user.getUsername()==null || user.getPassword()==null || user.getEmail()==null||user.getPhoneNumber()==null){
            System.out.println("유저 저장 실패");

        } else{
            UUID key = user.getId();
            users.put(key, user);
            System.out.println("유저 저장 성공");
        }

    }



    @Override
    public void removeUser(User user) {
        UUID userId = user.getId();
        if (users.containsKey(userId)){
            users.remove(userId);
            System.out.println("유저 데이터 삭제 완료");
        } else {
            System.out.println("유저 정보 없음");
        }

    }

    @Override
    public List<User> getAllUser() {

        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(UUID id) {
        if(users.containsKey(id)) {
            System.out.println("유저 정보 찾기 성공");
            return users.get(id);
        }
        else {
            System.out.println("유저 정보 찾기 실패");
            return null;
        }
    }

    @Override
    public void updateUser(UUID id, User user) {
        if (!users.containsKey(id)){
            System.out.println("등록된 유저가 없습니다");
        } else {
            User findUser = users.get(id);
            users.put(id, user);
            System.out.println("유저 정보 변경 성공");

        }

    }

    public void sendMessage(User sender, MessageRoom room, String content){
        Message message1 = new Message(sender, content);
        room.update();
        room.getHistory().add(message1);
    }

    public void sendFriendRequest(User fromUser, User toUser){
        if(getUser(toUser.getId())==null){
            System.out.println("친구 추가하려는 유저가 존재하지 않습니다.");
            return;
        }
        if (fromUser.getFriends().contains(toUser)) {
            System.out.println("이미 친구입니다");
            return;
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(fromUser);
        friendRequest.setReceiver(toUser);

        toUser.getReceivedFriendRequests().add(friendRequest);

    }
    public List<FriendRequest> getReceivedFriendRequests(User user){
        List<FriendRequest> receivedFriendRequests = user.getReceivedFriendRequests();
        return receivedFriendRequests;
    }

    public void acceptFriendRequest(FriendRequest request){
        request.getSender().getFriends().add(request.getReceiver());
        request.getReceiver().getFriends().add(request.getSender());
        System.out.printf(request.getReceiver().getUsername()+"(이)와 "+request.getSender().getUsername()+"(이)가 친구가 되었습니다.\n");
    }



}
