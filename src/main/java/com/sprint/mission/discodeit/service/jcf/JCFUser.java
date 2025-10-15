package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ValidateService;

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
    private final ValidateService validateService;
    private final ArrayList<Message> messageDb;

    private final JCFDb jcfDb;




    public JCFUser(JCFDb jcfDb) {

        this.jcfDb = jcfDb;
        this.userDb = jcfDb.getUserDb();
        this.deletedUserDb = jcfDb.getDeletedUserDb();
        this.validateService = new JCFValidateOperator(jcfDb);
        this.channelDb = jcfDb.getChannelDb();
        this.messageDb = jcfDb.getMessageDb();

    }


    @Override
    public void createUser(User user) {
        if(user == null){
            System.out.println(NULL_INPUT);
            return;
        }
        if(validateService.isValidateUser(user)){
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

        System.out.println("User Info :" + user.toString());

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

            System.out.println("User Info: " + user.toString());
        }
    }

    @Override
    public void readAllUser() {
        if (userDb.isEmpty()) {
            System.out.println(USER_EMPTY);
            return;
        }
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
        if (!validateService.isValidateUser(user)) {
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




        try {
            BiConsumer<User, Object> editFunction = userElement.setter;
            Object oldContent = userElement.getter.apply(user);
            editFunction.accept(user, updatedContent);
            user.updateEntity();
            System.out.printf("유저 변경사항 : %s\n", user.getName());
            System.out.println("변경한 필드: "+ userElement.name()+ " 변경전: "+oldContent.toString() +" ==> 변경 후: "+updatedContent);

        } catch (ClassCastException e) {
            System.out.println("잘못된 타입을 입력했습니다. 올바른 입력값을 넣어주세요");
        }


    }

    @Override
    public void readUpdatedUser() {
        if (userDb.stream().noneMatch(u -> u.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT)) {
            System.out.println("No Updated User");
            return;
        }
        for (User user : userDb) {
            if (user.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT) {
                readUser(user);
                System.out.println(user.getName() + "Updated Time: " + " " + user.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedUser() {
        if (deletedUserDb.isEmpty()) {
            System.out.println("No Deleted User");
            return;
        }
        System.out.println("===Deleted User=== ");
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
        if (!validateService.isValidateChannel(channel)) {
            System.out.println(CHANNEL_NOT_EXIST + channel.getName());
            return;
        }
        if (!validateService.isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST+ user.getName());
            return;
        }
        System.out.println(user.getName() + " enters " + channel.getName() + " channel.");
        channel.addUserToChannel(user);
        user.addChannel(channel);
    }

    public void exitChannel(User user, Channel channel) {
        if (!validateService.isValidateChannel(channel)) {
            System.out.println(CHANNEL_NOT_EXIST+ channel.getName());
            return;
        }
        if (!validateService.isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }

        System.out.println(user.getName() + "exit " + channel.getName() + " channel.");
        channel.removeUserFromChannel(user);
        user.removeChannel(channel);

    }

}
