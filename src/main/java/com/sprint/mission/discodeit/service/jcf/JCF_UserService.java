package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCF_UserService implements UserService {
    private static JCF_UserService service = new JCF_UserService();

    public final Map<UUID, User> data;
    private JCF_UserService() {
        data = new HashMap<>();
    }

    static public JCF_UserService getInstance() {
        return service;
    }

    //===============================
    //========== @Override ==========
    //===============================

    @Override
    public User creatUser(String name) {
        User user = new User(name);
        data.put(user.getId(), user);
        System.out.println( "\uD83C\uDF3C Creat user: " + name); // 🌼

        return user;
    }

    @Override
    public void getUser(String name) {
        this.data.values()
                .stream()
                .filter(user -> user.getUserName().equals(name))
                .findFirst()
                .ifPresent(user -> System.out.println( "\uD83C\uDF3C \uD83E\uDE77\uD83E\uDE77\uD83E\uDE77 readUser = " + user));
//        this.data.forEach((UUID key, User value) -> System.out.println()); //???
    }

    @Override
    public void getAllUsers() {
        System.out.println("\uD83C\uDF3C\uD83C\uDF3C readAllUsers \uD83C\uDF3C\uD83C\uDF3C"); // 🌼🌼
        this.data.values()
                .forEach(user -> System.out.println( "\uD83C\uDF3C " + user.getUserName())); // 🌼
    }

    @Override
    public void updateUser(String name, String reName) {

        this.data.values()
                .stream()
                .filter(user -> user.getUserName().equals(name))
                .findFirst()
                .ifPresent(user -> user.setUserName(reName));

        System.out.println("\uD83C\uDF3C update user: [" + name + "]에서 -> [" + reName + "]으로 변경"); // 🌼
    }

    @Override
    public void deleteUser(String name) {
        boolean isDeleted = data.values().removeIf(user -> user.getUserName().equals(name));
        String deletedMessage = (isDeleted) ? "⚠\uFE0F deleted user : " : "\uD83D\uDEA8 delete user err!! : ";

        System.out.println( deletedMessage + name); // ⚠️ 🚨
    }
}
