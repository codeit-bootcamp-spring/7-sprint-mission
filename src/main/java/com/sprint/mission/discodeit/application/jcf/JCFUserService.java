package com.sprint.mission.discodeit.application.jcf;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.domain.*;

import com.sprint.mission.discodeit.application.repository.UserRepository;
import com.sprint.mission.discodeit.application.UserService;




import java.util.*;

public class JCFUserService implements UserService {


    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
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
    public void update(UUID id, UserDto userDTO) {
        User user = findById(id);
        if (userDTO.getEmail() != null) {
            user.updateEmail(userDTO.getEmail());
        }
        if (userDTO.getNickname() != null) {
            user.updateNickname(userDTO.getNickname());
        }
        if (userDTO.getPassword() != null) {
            user.updatePassword(userDTO.getPassword());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.updatePhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getUsername() != null) {
            user.updateUsername(userDTO.getUsername());
        }

    }

    //이메일로 유저 정보 가져오기
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }



//    public Invitation sendFriendRequest(User fromUser, UUID toUserId) {
//        Invitation invitation = new Invitation(fromUser.getId(),toUserId, InvitationType.FRIEND_INVITATION);
//        User toUser = findById(toUserId);
//        fromUser.addMyInvitation(invitation);
//        toUser.addMyInvitation(invitation);
//        return invitation;
//    }
//
//
//
//    public void acceptFriendRequest(Invitation invitation) {
//        UUID receiverId = invitation.getReceiverId();
//        User user = findById(receiverId);
//        user.removeMyInvitation(invitation);
//        User sender = findById(invitation.getSenderId());
//        user.addFriend(invitation.getSenderId());
//        sender.addFriend(receiverId);
//
//    }
//
//
//
//        public void sendMessage(User sender, MessageRoom room, String content){
//        Message message1 = new Message(sender.getId(), content);
//        room.addHistory(message1);
//    }

}
