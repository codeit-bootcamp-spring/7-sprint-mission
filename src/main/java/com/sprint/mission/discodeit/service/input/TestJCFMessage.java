package com.sprint.mission.discodeit.service.input;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;

import static com.sprint.mission.discodeit.static_.StaticString.WRONG_INPUT;

public class TestJCFMessage {

    private final TestUtil testUtil;

    private final JCFMessage jcfMessage;


    public TestJCFMessage(TestUtil testUtil, JCFMessage jcfMessage) {
        this.testUtil = testUtil;
        this.jcfMessage = jcfMessage;
    }

    void testCreateMessage(String[] cmdArray){
        if (cmdArray.length != 4 || !testUtil.checkValidateBoolean(cmdArray[3])) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfMessage.createMessage(new Message(cmdArray[1], testUtil.targetUser(cmdArray[2]), Boolean.parseBoolean(cmdArray[3])));
        return;
    }
    void testReadMessage(String[] cmdArray){

        if (cmdArray.length != 2) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfMessage.readMessage(testUtil.targetMessage(cmdArray[1]));
    }
    void testReadAllMessage(){
        jcfMessage.readAllMessage();
    }
    void testUpdateMessage(String[] cmdArray){
        if (cmdArray.length != 4) {
            System.out.println(WRONG_INPUT);
            return;
        }
        Message targetMessage2 = testUtil.targetMessage(cmdArray[1]);
        Message.messageElement messageElement;
        try {
            messageElement = Message.messageElement.valueOf(cmdArray[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(WRONG_INPUT);
           return;
        }
        if (messageElement == Message.messageElement.CONTENT) {
            jcfMessage.updateMessage(targetMessage2, messageElement, cmdArray[3]);
        }
        if (messageElement == Message.messageElement.IS_MARKDOWN) {
            jcfMessage.updateMessage(targetMessage2, messageElement, Boolean.parseBoolean(cmdArray[3]));
        }
        return;
    }
    void testDeleteMessage(String[] cmdArray){
        if (cmdArray.length != 2) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfMessage.deleteMessage(testUtil.targetMessage(cmdArray[1]));
        return;
    }
    void testReadUpdatedMessage(){
        jcfMessage.readUpdatedMessage();
    }
    void testReadDeletedMessage(){
        jcfMessage.readDeletedMessage();
    }
}
