package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.awt.*;
import java.util.*;
import java.util.List;

public class JCFUserService implements UserService {
     private final List<User> users= new LinkedList<>();

    public JCFUserService() {

    }


        @Override
    public void create(String userId,String password,String userName,String userNickname) {
     users.add(new User(userId,password, userName, userNickname)) ;

    }

    @Override
    public void read(User user) {
        System.out.println(user.toString());
    }

    @Override
    public void readAll(List<User> users) {
        System.out.println("유저 리스트");
        for (User user : users) {
            System.out.println(" - " + user.getUserNickname());
        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {
              users.remove(user);
    }
}
