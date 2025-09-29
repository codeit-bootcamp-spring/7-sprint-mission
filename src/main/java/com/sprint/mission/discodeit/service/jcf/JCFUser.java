package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JCFUser implements UserService {
    public static final User DELETED_USER = new User("Deleted User", "Deleted User", "Deleted User Email", false);
    public final ArrayList<User> userDB;
    final Map<UUID,String> deletedUser = new HashMap<>();

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
        if(deletedUser.containsKey(user.getId())){
            System.out.println(user.getName()+ ": 삭제된 유저입니다.");
            return;
        }
        if(userDB.stream()
                .noneMatch(m->m.getId()==user.getId())){
            System.out.println(" 유저 없습니다. :" + user.getName());
            return;
        }

        System.out.println("유저 정보 :"+ user.toString() );

    }

    public void readUser(User ... users){
        for(User user : users){
            if(deletedUser.containsKey(user.getId())){
                System.out.println(user.getName() + ": 삭제된 유저입니다.");
                continue;
            }
            if(userDB.stream()
                    .noneMatch(m->m.getId()==user.getId())){
                System.out.println("유저 없습니다. : " + user.getName());
                continue;
            }

            System.out.println("유저 정보: "+ user.toString() );
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
                .noneMatch(u->u.getId()==user.getId())) {
            System.out.println("유저는 존재하지 않습니다. : " + user.getName());
            return;
        }
        userDB.remove(user);
        deletedUser.put(user.getId(),user.getName());
        System.out.printf("유저를 삭제합니다. : %s\n", user.getName());

    }

    @Override
    public <T> void updateUser(User user, User.userElement userElement, T updatedContent) {
        if (userDB.stream()
                .noneMatch(u->u.getId()==user.getId())) {
            System.out.println("유저는 존재하지 않습니다. : " + user.getName());
            return;
        }

        BiConsumer<User, Object> editFunction = userElement.setter;
        try{
            editFunction.accept(user, updatedContent);
            user.updateEntity();
            System.out.printf("유저 변경사항 : %s\n", user.getName());
        }
        catch (ClassCastException e){
            System.out.println("잘못된 타입을 입력했습니다. 올바른 입력값을 넣어주세요");
        }


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
                System.out.println(user.getName() + " 변경 시간: " + " " + user.getUpdatedAt());
            }
        }

    }

    @Override
    public void readDeletedUser() {
        if(deletedUser.isEmpty()) {
            System.out.println( "삭제된 유저가 없습니다.");
            return;
        }
        System.out.println( "===삭제된 유저=== ");
        for(UUID tmp :deletedUser.keySet())
        {
            String value = deletedUser.get(tmp);
            System.out.println(value);
        }
        System.out.println("==========");


    }
}
