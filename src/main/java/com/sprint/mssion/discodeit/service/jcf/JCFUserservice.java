package com.sprint.mssion.discodeit.service.jcf;

import com.sprint.mssion.discodeit.entity.Channel;
import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.service.UserService;

import java.util.*;

import static com.sprint.mssion.discodeit.service.jcf.JCFChannelService.channelRepo;
import static com.sprint.mssion.discodeit.service.jcf.JCfMessageService.msgRepo;

public class JCFUserservice implements UserService {
    public final static Map<UUID, User> userRepo;

    static {
        userRepo = new HashMap<>();
    }

    @Override
    public User create(String username, String password, String email, String phoneNumbers, String pronoun) {
        System.out.println("유저 생성");
        User newUser = new User(username, email, phoneNumbers, pronoun);
        userRepo.put(newUser.getCommon().getId(), newUser);
        return newUser;
    }

    @Override
    public User read(UUID userId) { // 단건 검색
        System.out.println("유저 단건 검색");
        if (userRepo.containsKey(userId)) {
            return userRepo.get(userId);
        }
        throw new NoSuchElementException("찾을 수 없는 유저: " + userId);
    }

    @Override
    public List<User> readAll() {
        System.out.println("유저 전체 검색");
        List<User> users = new ArrayList<>();
        for (UUID key : userRepo.keySet()) {
            User user = userRepo.get(key);
            users.add(user);
        }
        return users;
    }

    @Override
    public void update(UUID userId, String username, String password, String email, String phoneNumbers, String pronoun) {
        System.out.println("유저 업데이트");
        if (userRepo.containsKey(userId)) {
            User user = userRepo.get(userId);
            user.setUsername(username);
            user.setEmail(email);
            user.setPhoneNumbers(phoneNumbers);
            user.setPronoun(pronoun);
            user.getCommon().touch();
            userRepo.put(userId, user);
            System.out.println("변경");
        } else
            throw new NoSuchElementException("찾을 수 없는 유저: " + userId);
    }

    @Override
    public void delete(UUID userId) {
        System.out.println("유저 삭제");
        if (userRepo.containsKey(userId)) {
            userRepo.remove(userId);
            for (UUID key : channelRepo.keySet()) {
                Channel channel = channelRepo.get(key);
                channel.removeJoiner(userId);
            }
            Iterator<UUID> msgIt = msgRepo.keySet().iterator();
            while (msgIt.hasNext()) {
                UUID key = msgIt.next();
                Message msg = msgRepo.get(key);
                if (msg.getUserId().equals(userId)) {
                    msgIt.remove();
                }
            }
            System.out.println("유저 삭제 완료");
        } else
            throw new NoSuchElementException("찾을 수 없는 유저: " + userId);
    }

}
