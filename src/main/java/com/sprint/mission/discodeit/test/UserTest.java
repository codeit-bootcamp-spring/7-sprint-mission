package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFDb;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

public class UserTest {

    public static void main(String[] args) {
        JCFDb jcfDb = new JCFDb();
        JCFUser userService = new JCFUser(jcfDb);
        User user1 = new User("황준영","hwang","genius5375@gmail.com",true);
        User user2 = new User("대상혁","Faker" ,"faker@riot.org" , false);
        User user3 = new User("신창섭", "정상화","Maple.org" ,true);
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        userService.updateUser(user1, User.userElement.EMAIL,"OfficialHwempire.github.io");
        userService.updateUser(user2, User.userElement.ONLINE,true);

        userService.readDeletedUser();
    }
}
