package com.sprint.mission.discodeit.service.input;

import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFDb;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

import static com.sprint.mission.discodeit.static_.StaticString.*;

public class InputApplication {
    private final InputHandler inputHandler;

    private final JCFDb jcfDb;

    private final TestJCFChannel testJCFChannel;
    private final TestJCFMessage testJCFMessage;
    private final TestJCFUser testJCFUser;
    private final TestUtil testUtil;
    private final HelpOperator helpOperator ;
    private boolean isRunning = true;

    public InputApplication() {
        this.inputHandler = new InputHandler();
        this.jcfDb = new JCFDb();

        this.testUtil = new TestUtil(jcfDb);


        this.testJCFChannel = new TestJCFChannel(testUtil, new JCFChannel(jcfDb));
        this.testJCFMessage = new TestJCFMessage(testUtil, new JCFMessage(jcfDb));
        this.testJCFUser = new TestJCFUser(testUtil, new JCFUser(jcfDb));
        this.helpOperator = new HelpOperator();
    }

    public void start() {

        System.out.println("테스트를 시작합니다. 명령어를 입력하세요. !help을 통해 명령어를 확인할 수 있어요");
        while (isRunning) {
            String input = inputHandler.readLine();
            if (input.isEmpty()) continue;
            mappingCmd(input);


        }
        closeTest();
    }

    public void mappingCmd(String cmd) {
        String[] cmdArray = cmd.split(" ");


        String noUpperCase = cmdArray[0].toLowerCase();
        switch (noUpperCase) {
            case "createuser":
             testJCFUser.testCreateUser(cmdArray);
                break;
            case "readalluser":
                testJCFUser.testReadAllUser();
                break;
            case "deleteuser":
              testJCFUser.testDeleteUser(cmdArray);
                break;
            case "updateuser":
            testJCFUser.testUpdateUser(cmdArray);
                break;
            case "readupdateduser":
              testJCFUser.testReadUpdatedUser();
                break;
            case "readdeleteduser":
             testJCFUser.testReadDeletedUser();
                break;
            case "readuser":
              testJCFUser.testReadUser(cmdArray);
                break;
            case "enterchannel":
              testJCFUser.testEnterChannel(cmdArray);
                break;
            case "exitchannel":
              testJCFUser.testExitChannel(cmdArray);
                break;
            case "createchannel":
             testJCFChannel.testCreateChannel(cmdArray);
                break;
            case "readallchannel":
               testJCFChannel.testReadAllChannel();
                break;
            case "deletechannel":
            testJCFChannel.testDeleteChannel(cmdArray);
                break;
            case "readchannel":
              testJCFChannel.testReadChannel(cmdArray);
                break;
            case "updatechannel":
               testJCFChannel.testUpdateChannel(cmdArray);
                break;
            case "readupdatedchannel":
               testJCFChannel.testReadUpdatedChannel();
                break;
            case "readdeletedchannel":
               testJCFChannel.testReadDeletedChannel();
                break;
            case "inviteusertochannel":
              testJCFChannel.testInviteUserToChannel(cmdArray);
                break;
            case "deleteuserfromchannel":
             testJCFChannel.testDeleteUserFromChannel(cmdArray);
             break;
            case "createmessage":
             testJCFMessage.testCreateMessage(cmdArray);
                break;
            case "readmessage":
              testJCFMessage.testReadMessage(cmdArray);
                break;
            case "readallmessage":
               testJCFMessage.testReadAllMessage();
                break;
            case "deletemessage":
             testJCFMessage.testDeleteMessage(cmdArray);
                break;
            case "updatemessage":
               testJCFMessage.testUpdateMessage(cmdArray);
                break;
            case "readupdatedmessage":
                testJCFMessage.testReadUpdatedMessage();
                break;
            case "readdeletedmessage":
                testJCFMessage.testReadDeletedMessage();
                break;
            case "!help":
               helpOperator.helpFinal(cmdArray);
               break;
            case"!exit":
                isRunning = false;
            default:
                System.out.println(WRONG_COMMAND);


        }
    }

    public void closeTest() {

        inputHandler.close();
        System.out.println("테스트 종료");
    }

//    public boolean checkValidateBoolean(String input) {
//
//        if (input == null) return false;
//        String inputLowerCase = input.toLowerCase();
//        return inputLowerCase.equals("true") || inputLowerCase.equals("false");
//    }
//
//    public Channel targetChannel(String channelName) {
//
//        return jcfDb.getChannelDb()
//                .stream()
//                .filter(m -> m.getName().equals(channelName)).findFirst().orElse(null);
//    }
//
//    public User targetUser(String userName) {
//        return jcfDb.getUserDb()
//                .stream()
//                .filter(m -> m.getName().equals(userName)).findFirst().orElse(null);
//    }
//
//    public Message targetMessage(String messageContent) {
//        return jcfDb.getMessageDb()
//                .stream()
//                .filter(m -> m.getContent().equals(messageContent)).findFirst().orElse(null);
//    }

//    public void showHelp() {
//        System.out.println("!help 명령어 : !help  또는 !help <channel/user/message>");
//        System.out.println("\n ");
//        showChannelHelp();
//        System.out.print("\n\n");
//        showMessageHelp();
//        System.out.print("\n\n");
//        showUserHelp();
//    }
//
//    public void showChannelHelp() {
//        System.out.println("=========Channel 명령어 입니다==========");
//        System.out.println("createchannel <channel_name> <description> <is_public> <is_text_channel>");
//        System.out.println("readallchannel");
//        System.out.println("deletechannel <channel_name>");
//        System.out.println("readchannel <channel_name>");
//        System.out.println("updatechannel <channel_name> <channel_element> <update content>>");
//        System.out.println("readupdatedchannel");
//        System.out.println("readdeletedchannel");
//        System.out.println("inviteusertochannel <user_name> <channel_name>");
//        System.out.println("deleteuserfromchannel <user_name> <channel_name>");
//        System.out.println("=========================== ");
//    }
//
//    public void showUserHelp() {
//        System.out.println("=========User 명령어 입니다==========");
//        System.out.println("createuser <user_name> <nickname> <email> <is_online>");
//        System.out.println("readalluser");
//        System.out.println("deleteuser <user_name>");
//        System.out.println("updateuser <user_name> <user_element> <update content  >");
//        System.out.println("readupdateduser");
//        System.out.println("readdeleteduser");
//        System.out.println("readuser <user_name>");
//        System.out.println("enterchannel <user_name> <channel_name>");
//        System.out.println("exitchannel <user_name> <channel_name>");
//        System.out.println("=========================== ");
//
//
//    }
//
//    public void showMessageHelp() {
//        System.out.println("=========Message 명령어 입니다==========");
//        System.out.println("createmessage <user_name> <content> <is_markdown>");
//        System.out.println("readmessage <message_content>");
//        System.out.println("readallmessage");
//        System.out.println("deletemessage <message_content>");
//        System.out.println("updatemessage <message_content> <message_element> <update content>");
//        System.out.println("readupdatedmessage");
//        System.out.println("readdeletedmessage");
//        System.out.println("=========================== ");
//
//    }


}
