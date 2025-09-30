package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.etc.StaticString;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.etc.StaticString.*;

public class JCFUser implements UserService {
    public static final User DELETED_USER = new User("Deleted User", "Deleted User", "Deleted User Email", false);
    private final ArrayList<User> userDB;
    private final Map<UUID, String> deletedUser;

    private final JCFDb jcfDb;




    public JCFUser(JCFDb jcfDb) {

        this.jcfDb = jcfDb;
        this.userDB = jcfDb.getUserDb();
        this.deletedUser = jcfDb.getDeletedUserDb();
    }

    public <T> Object getUserContent(User user, User.userElement userElement) {
        switch (userElement) {
            case NAME:
                return user.getName();
            case NICKNAME:
                return user.getNickname();
            case ONLINE:
                return user.isOnline();
            case EMAIL:
                return user.getEmail();
            default:
                throw new IllegalArgumentException(WRONG_TYPE);



        }
    }

    @Override
    public void createUser(User user) {
        jcfDb.createUser(user);

    }

    @Override
    public void readUser(User user) {
        if (deletedUser.containsKey(user.getId())) {
            System.out.println(user.getName() + USER_ALREADY_DELETED);
            return;
        }
        if (userDB.stream()
                .noneMatch(m -> m.getId() == user.getId())) {
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }

        System.out.println("유저 정보 :" + user.toString());

    }

    public void readUser(User... users) {
        for (User user : users) {
            if (deletedUser.containsKey(user.getId())) {
                System.out.println(user.getName() + USER_ALREADY_DELETED);
                continue;
            }
            if (userDB.stream()
                    .noneMatch(m -> m.getId() == user.getId())) {
                System.out.println(USER_NOT_EXIST + user.getName());
                continue;
            }

            System.out.println("유저 정보: " + user.toString());
        }
    }

    @Override
    public void readAllUser() {
        for (User user : userDB) {
            readUser(user);
        }

    }

    @Override
    public void deleteUser(User user) {
        jcfDb.deleteUser(user);

    }

    @Override
    public <T> void updateUser(User user, User.userElement userElement, T updatedContent) {
        if (userDB.stream()
                .noneMatch(u -> u.getId() == user.getId())) {
            System.out.println("유저는 존재하지 않습니다. : " + user.getName());
            return;
        }

        BiConsumer<User, Object> editFunction = userElement.setter;
        Object oldContent = getUserContent(user, userElement);

        try {
            System.out.printf("유저 변경사항 : %s\n", user.getName());
            System.out.println("변경한 필드: "+ userElement.name()+ " 변경전: "+oldContent +" ==> 변경 후: "+updatedContent);
            editFunction.accept(user, updatedContent);
            user.updateEntity();

        } catch (ClassCastException e) {
            System.out.println("잘못된 타입을 입력했습니다. 올바른 입력값을 넣어주세요");
        }


    }

    @Override
    public void readUpdatedUser() {
        if (userDB.stream().noneMatch(u -> u.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT)) {
            System.out.println("업데이트 된 유저가 없습니다.");
            return;
        }
        for (User user : userDB) {
            if (user.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT) {
                readUser(user);
                System.out.println(user.getName() + " 변경 시간: " + " " + user.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedUser() {
        if (deletedUser.isEmpty()) {
            System.out.println("삭제된 유저가 없습니다.");
            return;
        }
        System.out.println("===삭제된 유저=== ");
        for (UUID tmp : deletedUser.keySet()) {
            String value = deletedUser.get(tmp);
            System.out.println(value);
        }
        System.out.println("==========");


    }

    public void enterChannel(User user, Channel channel) {
        if (!jcfDb.isValidateChannel(channel)) {
            System.out.println(CHANNEL_NOT_EXIST + channel.getName());
            return;
        }
        if (!jcfDb.isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST+ user.getName());
            return;
        }
        System.out.println(user.getName() + "님이 " + channel.getName() + " 채널에 입장하였습니다.");
        channel.addUserToChannel(user);
        user.addChannel(channel);
    }

    public void exitChannel(User user, Channel channel) {
        if (!jcfDb.isValidateChannel(channel)) {
            System.out.println(CHANNEL_NOT_EXIST+ channel.getName());
            return;
        }
        if (!jcfDb.isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }

        channel.removeUserFromChannel(user);
        user.removeChannel(channel);

    }

}
