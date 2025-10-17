package com.sprint.mission.discodeit.service.input;

public class TestJCFMessage {

//    private final TestUtil testUtil;
//
////    private final JCFMessage jcfMessage;
//    private final MessageService messageService;
//
//
//    public TestJCFMessage(TestUtil testUtil, MessageService messageService) {
//        this.testUtil = testUtil;
//        this.messageService = messageService;
//    }
//
//    void testCreateMessage(String[] cmdArray){
//        if (cmdArray.length != 4 || !testUtil.checkValidateBoolean(cmdArray[3])) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        User targetUser = testUtil.targetUser(cmdArray[2]);
//        if (targetUser == null) {
//            System.out.println(USER_NOT_EXIST + cmdArray[2] );
//            return;
//        }
//        messageService.createMessage(new Message(cmdArray[1], testUtil.targetUser(cmdArray[2]), Boolean.parseBoolean(cmdArray[3])));
//        return;
//    }
//    void testReadMessage(String[] cmdArray){
//
//        if (cmdArray.length != 2) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        messageService.readMessage(testUtil.targetMessage(cmdArray[1]));
//    }
//    void testReadAllMessage(){
//        messageService.readAllMessage();
//    }
//    void testUpdateMessage(String[] cmdArray){
//        if (cmdArray.length != 4) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        Message targetMessage2 = testUtil.targetMessage(cmdArray[1]);
//        Message.messageElement messageElement;
//        try {
//            messageElement = Message.messageElement.valueOf(cmdArray[2].toUpperCase());
//        } catch (IllegalArgumentException e) {
//            System.out.println(WRONG_INPUT);
//           return;
//        }
//        if (messageElement == Message.messageElement.CONTENT) {
//            messageService.updateMessage(targetMessage2, messageElement, cmdArray[3]);
//        }
//        if (messageElement == Message.messageElement.IS_MARKDOWN) {
//            messageService.updateMessage(targetMessage2, messageElement, Boolean.parseBoolean(cmdArray[3]));
//        }
//        return;
//    }
//    void testDeleteMessage(String[] cmdArray){
//        if (cmdArray.length != 2) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        messageService.deleteMessage(testUtil.targetMessage(cmdArray[1]));
//        return;
//    }
//    void testReadUpdatedMessage(){
//        messageService.readUpdatedMessage();
//    }
//    void testReadDeletedMessage(){
//        messageService.readDeletedMessage();
//    }
}
