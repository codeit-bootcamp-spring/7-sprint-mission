package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService{
    private final List<User> userStore = new ArrayList<>();
    private final MessageService messageService;

    public JCFUserService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void createUser(String userName, String nickName, String email, String phoneNum, String userId, String password) {
        // 유저 닉네임 중복 확인 코드 필요
        // 유저 아이디 중복 확인 코드 필요

        User newUser = new User(userName, nickName, email, phoneNum, userId, password);
        userStore.add(newUser);
    }

    @Override
    public User getUserByEmail(String email) {
        User target = userStore.stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
        return target;
    }

    @Override
    public User getUserByPhone(String phoneNum) {
        User target = userStore.stream()
                .filter(u -> phoneNum.equals(u.getPhoneNum()))
                .findFirst()
                .orElse(null);
        return target;
    }

    @Override
    public User getUserByUserId(String userId) {
        User target = userStore.stream()
                .filter(u -> userId.equals(u.getUserId()))
                .findFirst()
                .orElse(null);
        return target;
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStore;
    }

    @Override
    public void updateUser(User user) {
        for(int i = 0; i < userStore.size(); i++){
            if(userStore.get(i).getId() == user.getId()){ //UUID 비교
                //변경될 사용자 정보가 비어있는 경우 이전 정보 저장
                if(user.getUserName() == null) user.setUserName(userStore.get(i).getUserName());
                else if(user.getNickName() == null) user.setNickName(userStore.get(i).getNickName());
                else if(user.getEmail() == null) user.setEmail(userStore.get(i).getEmail());
                else if(user.getPhoneNum() == null) user.setPhoneNum(userStore.get(i).getPhoneNum());
                else if(user.getPassword() == null) user.setPassword(userStore.get(i).getPassword());

                //변경된 사용자 정보로 업데이트
                userStore.set(i, user);
            }
        }
    }

    @Override
    public void deleteUser(UUID id) {
        for(int i = 0; i < userStore.size(); i++){
            if(userStore.get(i).getId() == id){
                userStore.remove(i);
                messageService.delMessageByUser(userStore.get(i)); //삭제될 유저가 보낸 메시지도 삭제
                break;
            }
        }
    }

    @Override
    public void removeChannelFromUser(UUID id, User user) {
        user.delChannelId(id);
    }
}
