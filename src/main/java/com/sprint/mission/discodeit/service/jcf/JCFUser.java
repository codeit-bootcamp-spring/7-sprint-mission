package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class JCFUser implements UserService {

    public final ArrayList<User> userDB;
    final ArrayList<String> deletedUser = new ArrayList<>();

    public JCFUser(ArrayList<User> userDB) {
        this.userDB = userDB;
    }
    public JCFUser() {
        this.userDB = new ArrayList<>();
    }

    @Override
    public void createUser(User user) {
        userDB.add(user);
        System.out.printf("User created: %s\n", user.getName());

    }

    @Override
    public void readUser(User user) {
        if(userDB.stream()
                .noneMatch(u->u.getName().equals(user.getName()))){
            System.out.println("No such user");
            return;
        }
        System.out.println("User info: "+ user.toString() );

    }

    public void readUser(User ... users){
        for(User user : users){
            if(userDB.stream()
                    .noneMatch(u->u.getName().equals(user.getName()))){
                System.out.println("No such user");
                continue;
            }
            System.out.println("User info: "+ user.toString() );
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
        if (userDB.stream()
                .noneMatch(u -> u.getName().equals(user.getName()))) {
            System.out.println("No such user");
            return;
        }
        userDB.remove(user);
        deletedUser.add(user.getName());
        System.out.printf("User deleted: %s\n", user.getName());

    }

    @Override
    public <T> void updateUser(User user, User.userElement userElement, T updatedContent) {
        BiConsumer<User, Object> editFunction = userElement.setter;
        Class<? extends BiConsumer> aClass = editFunction.getClass();
        if(!aClass.isInstance(updatedContent)) {
            System.out.println("잘못된 타입을 입력했습니다");
            return;
        }
        editFunction.accept(user, updatedContent);
        user.updateEntity();
        System.out.printf("User updated: %s\n", user.getName());

    }

    @Override
    public void readUpdatedUser() {
        if(userDB.stream().noneMatch(u->u.getUpdatedAt()!=Entity.DEFAULT_UPDATED_AT)){
            System.out.println("업데이트 된 유저가 없습니다.");
            return;
        }
        for (User user : userDB) {
            if (user.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT) {
                readUser(user);
                System.out.println(user.getName() + " is Updated at: " + " " + user.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedUser() {
        System.out.println("Deleted User = " + deletedUser);

    }
}
