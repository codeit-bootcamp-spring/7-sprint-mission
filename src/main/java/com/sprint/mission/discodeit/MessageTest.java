package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFDb;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

import java.util.ArrayList;

public class MessageTest {
    public static void main(String[] args) {
        JCFDb jcfDb = new JCFDb();
        JCFUser userService = new JCFUser(jcfDb);
        JCFMessage messageService = new JCFMessage(jcfDb);
        User user1 = new User("황준영","hwang","genius5375@gmail.com",true);
        User user2 = new User("대상혁","Faker" ,"faker@riot.org" , false);
        User notInUserDBUser = new User("신창섭", "정상화","maple.org" ,true); //not in userDB ( "not in userDB)

        Message m1 = new Message("Hello", user1, false);
        Message m2 = new Message("Hi I am Faker", user2, false);
        Message m3 = new Message("JAVA 를 정상화하네", notInUserDBUser, false);

        userService.createUser(user1);
        userService.createUser(user2);

        messageService.createMessage(m1);
        messageService.createMessage(m2);
        messageService.createMessage(m3);

        userService.deleteUser(user1);

        messageService.readMessage(m1);


    }
}
