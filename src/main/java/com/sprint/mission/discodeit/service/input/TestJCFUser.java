package com.sprint.mission.discodeit.service.input;

public class TestJCFUser {
//    private final TestUtil testUtil;
////
//    private final UserService userService;
//
//    public TestJCFUser(TestUtil testUtil, UserService userService) {
//        this.testUtil = testUtil;
//        this.userService = userService;
//    }
//
//    void testCreateUser(String [] cmdArray){
//        if (cmdArray.length != 5 || !testUtil.checkValidateBoolean(cmdArray[4])) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        userService.createUser(new User(cmdArray[1], cmdArray[2], cmdArray[3], Boolean.parseBoolean(cmdArray[4])));
//        return;
//
//    }
//    void testReadAllUser(){
//        userService.readAllUser();
//    }
//    void testDeleteUser(String[] cmdArray){
//        if (cmdArray.length != 2) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        User user = testUtil.targetUser(cmdArray[1]);
//        if (user == null) {
//            System.out.println(USER_NOT_EXIST + cmdArray[1]);
//            return;
//        }
//        userService.deleteUser(user);
//       return;
//    }
//    void testReadUpdatedUser(){
//        userService.readUpdatedUser();
//    }
//    void testReadDeletedUser(){
//        userService.readDeletedUser();
//    }
//    void testEnterChannel(String[] cmdArray){
//
//        if (cmdArray.length != 3) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        User targetUser2 = testUtil.targetUser(cmdArray[1]);
//        Channel targetChannel = testUtil.targetChannel(cmdArray[2]);
//        userService.enterChannel(targetUser2, targetChannel);
//        return;
//    }
//    void testExitChannel(String[] cmdArray){
//
//        if (cmdArray.length != 3) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        User targetUser3 = testUtil.targetUser(cmdArray[1]);
//        Channel targetChannel2 = testUtil.targetChannel(cmdArray[2]);
//        userService.exitChannel(targetUser3, targetChannel2);
//        return;
//    }
//    void testUpdateUser(String[] cmdArray){
//        if (cmdArray.length != 4) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        User targetUser = testUtil.targetUser(cmdArray[1]);
//        if (targetUser == null) {
//            System.out.println(USER_NOT_EXIST + cmdArray[1]);
//            return;
//        }
//
//        User.userElement userElement;
//        try {
//            userElement = User.userElement.valueOf(cmdArray[2].toUpperCase());
//        } catch (
//                IllegalArgumentException e
//        ) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        if (userElement == User.userElement.ONLINE) {
//            userService.updateUser(targetUser, userElement, Boolean.parseBoolean(cmdArray[3]));
//            return;
//        }
//        userService.updateUser(targetUser, userElement, cmdArray[3]);
//        return;
//    }
//    void testReadUser(String[] cmdArray){
//        if (cmdArray.length != 2) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        userService.readUser(testUtil.targetUser(cmdArray[1]));
//
//    }
}
