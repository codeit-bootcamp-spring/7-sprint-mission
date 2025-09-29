package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

public class UserTest {

    public static void main(String[] args) {

        JCFUser userService = new JCFUser();
        User user1 = new User("황준영","hwang","genius5375@gmail.com",true);
        User user2 = new User("대상혁","Faker" ,"faker@riot.org" , false);
        User user3 = new User("신창섭", "정상화","Maple.org" ,true);
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.readAllUser();
        userService.userDB.stream()
                        .filter(User::isOnline)
                                .forEach(System.out::println);
        userService.deleteUser(user1);
        userService.deleteUser(user2);
        userService.deleteUser(user3);

        userService.readDeletedUser();
    }
}
