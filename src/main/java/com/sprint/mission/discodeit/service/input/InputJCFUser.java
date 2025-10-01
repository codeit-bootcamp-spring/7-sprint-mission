package com.sprint.mission.discodeit.service.input;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

import static com.sprint.mission.discodeit.static_.StaticString.USER_NOT_EXIST;
import static com.sprint.mission.discodeit.static_.StaticString.WRONG_INPUT;

public class InputJCFUser {
    private final InputUtil testUtil;
    private final JCFUser jcfUser;

    public InputJCFUser(InputUtil testUtil, JCFUser jcfUser) {
        this.testUtil = testUtil;
        this.jcfUser = jcfUser;
    }

    void testCreateUser(String [] cmdArray){
        if (cmdArray.length != 5 || !testUtil.checkValidateBoolean(cmdArray[4])) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfUser.createUser(new User(cmdArray[1], cmdArray[2], cmdArray[3], Boolean.parseBoolean(cmdArray[4])));
        return;

    }
    void testReadAllUser(){
        jcfUser.readAllUser();
    }
    void testDeleteUser(String[] cmdArray){
        if (cmdArray.length != 2) {
            System.out.println(WRONG_INPUT);
            return;
        }
        User user = testUtil.targetUser(cmdArray[1]);
        if (user == null) {
            System.out.println(USER_NOT_EXIST + cmdArray[1]);
            return;
        }
        jcfUser.deleteUser(user);
       return;
    }
    void testReadUpdatedUser(){
        jcfUser.readUpdatedUser();
    }
    void testReadDeletedUser(){
        jcfUser.readDeletedUser();
    }
    void testEnterChannel(String[] cmdArray){

        if (cmdArray.length != 3) {
            System.out.println(WRONG_INPUT);
            return;
        }
        User targetUser2 = testUtil.targetUser(cmdArray[1]);
        Channel targetChannel = testUtil.targetChannel(cmdArray[2]);
        jcfUser.enterChannel(targetUser2, targetChannel);
        return;
    }
    void testExitChannel(String[] cmdArray){

        if (cmdArray.length != 3) {
            System.out.println(WRONG_INPUT);
            return;
        }
        User targetUser3 = testUtil.targetUser(cmdArray[1]);
        Channel targetChannel2 = testUtil.targetChannel(cmdArray[2]);
        jcfUser.exitChannel(targetUser3, targetChannel2);
        return;
    }
    void testUpdateUser(String[] cmdArray){
        if (cmdArray.length != 4) {
            System.out.println(WRONG_INPUT);
            return;
        }
        User targetUser = testUtil.targetUser(cmdArray[1]);
        if (targetUser == null) {
            System.out.println(USER_NOT_EXIST + cmdArray[1]);
            return;
        }

        User.userElement userElement;
        try {
            userElement = User.userElement.valueOf(cmdArray[2].toUpperCase());
        } catch (
                IllegalArgumentException e
        ) {
            System.out.println(WRONG_INPUT);
            return;
        }
        if (userElement == User.userElement.ONLINE) {
            jcfUser.updateUser(targetUser, userElement, Boolean.parseBoolean(cmdArray[3]));
            return;
        }
        jcfUser.updateUser(targetUser, userElement, cmdArray[3]);
        return;
    }
    void testReadUser(String[] cmdArray){
        if (cmdArray.length != 2) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfUser.readUser(testUtil.targetUser(cmdArray[1]));

    }
}
