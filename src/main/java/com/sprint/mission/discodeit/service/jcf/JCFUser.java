package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.static_.StaticString.*;

public class JCFUser implements UserService {
    public static final User DELETED_USER = new User("Deleted User", "Deleted User", "Deleted User Email", false);
    private final ArrayList<User> userDb;
    private final Map<UUID, String> deletedUserDb;
    private final ArrayList<Channel> channelDb;
    private final JCFValidateOperator validateOperator;
    private final ArrayList<Message> messageDb;

    private final JCFDb jcfDb;




    public JCFUser(JCFDb jcfDb) {

        this.jcfDb = jcfDb;
        this.userDb = jcfDb.getUserDb();
        this.deletedUserDb = jcfDb.getDeletedUserDb();
        this.validateOperator = new JCFValidateOperator(jcfDb);
        this.channelDb = jcfDb.getChannelDb();
        this.messageDb = jcfDb.getMessageDb();

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
                return null;



        }
    }

    @Override
    public void createUser(User user) {
        if(user == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(validateOperator.isValidateUser(user)){
            System.out.println(USER_EXIST + user.getName());
            return;
        }
        userDb.add(user);
        System.out.println(CREATE_USER + user.getName());
        return;

    }

    @Override
    public void readUser(User user) {
        if (user == null) {
            System.out.println(NULL_INPUT);
            return;
        }
        if (deletedUserDb.containsKey(user.getId())) {
            System.out.println(user.getName() + USER_ALREADY_DELETED);
            return;
        }
        if (userDb.stream()
                .noneMatch(m -> m.getId() == user.getId())) {
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }

        System.out.println("유저 정보 :" + user.toString());

    }

    public void readUser(User... users) {
        for (User user : users) {
            if (user == null) {
                continue;
            }
            if (deletedUserDb.containsKey(user.getId())) {
                System.out.println(user.getName() + USER_ALREADY_DELETED);
                continue;
            }
            if (userDb.stream()
                    .noneMatch(m -> m.getId() == user.getId())) {
                System.out.println(USER_NOT_EXIST + user.getName());
                continue;
            }

            System.out.println("유저 정보: " + user.toString());
        }
    }

    @Override
    public void readAllUser() {
        for (User user : userDb) {
            readUser(user);
        }

    }

    @Override
    public void deleteUser(User user) {
        if (user == null) {
            System.out.println(NULL_INPUT);
            return;
        }
        if (!validateOperator.isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }
        userDb.remove(user);
        deletedUserDb.put(user.getId(), user.getName());

        channelDb.forEach(x->x.removeUserFromChannel(user));
//        channelDb.stream()
//                        .filter(x-> x.getUserDb().contains(user))
//                                .forEach(x->
//                                        new JCFChannel(this).deleteUserFromChannel(user,x)
//                                        );
        messageDb.forEach(x->x.setSender(JCFUser.DELETED_USER));

        System.out.println(DELETE_USER + user.getName());
        return;

    }

    @Override
    public <T> void updateUser(User user, User.userElement userElement, T updatedContent) {
        if(user == null || updatedContent == null || userElement == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if (userDb.stream()
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
        if (userDb.stream().noneMatch(u -> u.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT)) {
            System.out.println("업데이트 된 유저가 없습니다.");
            return;
        }
        for (User user : userDb) {
            if (user.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT) {
                readUser(user);
                System.out.println(user.getName() + " 변경 시간: " + " " + user.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedUser() {
        if (deletedUserDb.isEmpty()) {
            System.out.println("삭제된 유저가 없습니다.");
            return;
        }
        System.out.println("===삭제된 유저=== ");
        for (UUID tmp : deletedUserDb.keySet()) {
            String value = deletedUserDb.get(tmp);
            System.out.println(value);
        }
        System.out.println("==========");


    }

    public void enterChannel(User user, Channel channel) {
        if (user == null || channel == null) {
            System.out.println(NULL_INPUT);
            return;
        }
        if (!validateOperator.isValidateChannel(channel)) {
            System.out.println(CHANNEL_NOT_EXIST + channel.getName());
            return;
        }
        if (!validateOperator.isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST+ user.getName());
            return;
        }
        System.out.println(user.getName() + "님이 " + channel.getName() + " 채널에 입장하였습니다.");
        channel.addUserToChannel(user);
        user.addChannel(channel);
    }

    public void exitChannel(User user, Channel channel) {
        if (!validateOperator.isValidateChannel(channel)) {
            System.out.println(CHANNEL_NOT_EXIST+ channel.getName());
            return;
        }
        if (!validateOperator.isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }

        channel.removeUserFromChannel(user);
        user.removeChannel(channel);

    }

}
