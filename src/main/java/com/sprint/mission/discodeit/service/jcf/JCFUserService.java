package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.*;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.vo.Invitation;
import com.sprint.mission.discodeit.vo.InvitationType;
import com.sprint.mission.discodeit.vo.Message;


import java.util.*;

public class JCFUserService implements UserService {


    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        if(user.getUsername()==null || user.getPassword()==null || user.getEmail()==null||user.getPhoneNumber()==null) {
            throw new IllegalArgumentException("데이터를 모두 입력해 주세요.");
        }
        userRepository.save(user);
        System.out.println("유저 저장 성공");
    }

    @Override
    public void remove(User entity) {
        if(findById(entity.getId())==null){
            return;
        }
        userRepository.remove(entity);
        System.out.println("유저 삭제 성공");
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(()->new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(UUID id, UserDTO userDTO) {
        User user = findById(id);
        if (userDTO.getEmail()!=null){
            System.out.println("이메일 변경 성공");
            user.setEmail(userDTO.getEmail());
        }
        if(userDTO.getNickname()!=null){
            System.out.println("닉네임 변경 성공");
            user.setNickname(userDTO.getNickname());
        }
        if(userDTO.getPassword()!=null){
            System.out.println("비밀번호 변경 성공");
            user.setPassword(user.getPassword());
        }
        if(userDTO.getPhoneNumber()!=null){
            System.out.println("번호 변경 성공");
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getUsername()!=null){
            System.out.println("이름 변경 성공");
            user.setUsername(userDTO.getUsername());
        }
    }


    public Invitation sendFriendRequest(User fromUser, UUID toUserId) {
        Invitation invitation = new Invitation(fromUser.getId(),toUserId, InvitationType.FRIEND_INVITATION);
        User toUser = findById(toUserId);
        fromUser.addMyInvitation(invitation);
        toUser.addMyInvitation(invitation);
        return invitation;
    }



    public void acceptFriendRequest(Invitation invitation) {
        UUID receiverId = invitation.getReceiverId();
        User user = findById(receiverId);
        user.removeMyInvitation(invitation);
        User sender = findById(invitation.getSenderId());
        user.addFriend(invitation.getSenderId());
        sender.addFriend(receiverId);

    }



        public void sendMessage(User sender, MessageRoom room, String content){
        Message message1 = new Message(sender.getId(), content);
        room.addHistory(message1);
    }

}
