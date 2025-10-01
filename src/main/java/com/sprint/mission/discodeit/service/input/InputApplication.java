package com.sprint.mission.discodeit.service.input;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFDb;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;
import com.sprint.mission.discodeit.static_.StaticString;

import java.util.StringTokenizer;
import java.util.function.BooleanSupplier;

import static com.sprint.mission.discodeit.static_.StaticString.*;

public class InputApplication {
    private final InputHandler inputHandler;
    private final JCFUser jcfUser;
    private final JCFDb jcfDb;
    private final JCFChannel jcfChannel;
    private final JCFMessage jcfMessage;

    public InputApplication() {
        this.inputHandler = new InputHandler();
        this.jcfDb = new JCFDb();
        this.jcfUser = new JCFUser(jcfDb);

        this.jcfChannel = new JCFChannel(jcfDb);
        this.jcfMessage = new JCFMessage(jcfDb);
    }

    public void start() {
        boolean isRunning = true;
        System.out.println("테스트를 시작합니다. 명령어를 입력하세요. !help을 통해 명령어를 확인할 수 있어요");
        while (isRunning) {
            String input = inputHandler.readLine();
            if (input.isEmpty()) continue;
            mappingCmd(input);

        }
    }

    public void mappingCmd(String cmd) {
        String[] cmdArray = cmd.split(" ");


        String noUpperCase = cmdArray[0].toLowerCase();
        switch (noUpperCase) {
            case "createuser":
                if (cmdArray.length != 5 || !checkValidateBoolean(cmdArray[4])) {
                    System.out.println(WRONG_INPUT);

                    break;
                }
                jcfUser.createUser(new User(cmdArray[1], cmdArray[2], cmdArray[3], Boolean.parseBoolean(cmdArray[4])));
                break;
            case "readalluser":
                jcfUser.readAllUser();
                break;
            case "deleteuser":
                if (cmdArray.length != 2) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                User user = targetUser(cmdArray[1]);
                if (user == null) {
                    System.out.println(USER_NOT_EXIST + cmdArray[1]);
                    break;
                }
                jcfUser.deleteUser(user);
                break;
            case "updateuser":
                if (cmdArray.length != 4) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                User targetUser = targetUser(cmdArray[1]);
                if (targetUser == null) {
                    System.out.println(USER_NOT_EXIST + cmdArray[1]);
                    break;
                }

                User.userElement userElement;
                try {
                    userElement = User.userElement.valueOf(cmdArray[2].toUpperCase());
                } catch (
                        IllegalArgumentException e
                ) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                if (userElement == User.userElement.ONLINE) {
                    jcfUser.updateUser(targetUser, userElement, Boolean.parseBoolean(cmdArray[3]));
                    break;
                }
                jcfUser.updateUser(targetUser, userElement, cmdArray[3]);
                break;
            case "readupdateduser":
                jcfUser.readUpdatedUser();
                break;
            case "readdeleteduser":
                jcfUser.readDeletedUser();
                break;
            case "readuser":
                if (cmdArray.length != 2) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                jcfUser.readUser(targetUser(cmdArray[1]));
            case "enterchannel":
                if (cmdArray.length != 3) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                User targetUser2 = jcfDb.getUserDb()
                        .stream()
                        .filter(m -> m.getName().equals(cmdArray[1])).findFirst()
                        .get();
                Channel targetChannel = jcfDb.getChannelDb()
                        .stream()
                        .filter(m -> m.getName().equals(cmdArray[2])).findFirst().get();
                jcfUser.enterChannel(targetUser2, targetChannel);
                break;
            case "exitchannel":
                if (cmdArray.length != 3) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                User targetUser3 = jcfDb.getUserDb()
                        .stream()
                        .filter(m -> m.getName().equals(cmdArray[1])).findFirst()
                        .get();
                Channel targetChannel2 = jcfDb.getChannelDb()
                        .stream()
                        .filter(m -> m.getName().equals(cmdArray[2])).findFirst().get();
                jcfUser.exitChannel(targetUser3, targetChannel2);
                break;
            case "createchannel":
                if (cmdArray.length != 5 || !checkValidateBoolean(cmdArray[3]) || !checkValidateBoolean(cmdArray[4])) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                jcfChannel.createChannel(new Channel(cmdArray[1], cmdArray[2], Boolean.parseBoolean(cmdArray[3]), Boolean.parseBoolean(cmdArray[4])));
                break;
            case "readallchannel":
                jcfChannel.readAllChannel();
                break;
            case "deletechannel":
                if (cmdArray.length != 2) {
                    System.out.println(WRONG_INPUT);
                    break;
                }

                jcfChannel.deleteChannel(targetChannel(cmdArray[1]));
                break;
            case "readchannel":
                if (cmdArray.length != 2) {
                    System.out.println(WRONG_INPUT);
                    break;

                }
                jcfChannel.readChannel(targetChannel(cmdArray[1]));
                break;
            case "updatechannel":
                if (cmdArray.length != 4) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                Channel targetChannel3 = targetChannel(cmdArray[1]);

                Channel.channelElement channelElement;
                try {
                    channelElement = Channel.channelElement.valueOf(cmdArray[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println(WRONG_INPUT);
                    break;
                }

                if (channelElement == Channel.channelElement.IS_PUBLIC) {
                    jcfChannel.updateChannel(targetChannel3, channelElement, Boolean.parseBoolean(cmdArray[3]));

                }
                if (channelElement == Channel.channelElement.IS_TEXT_CHANNEL) {
                    jcfChannel.updateChannel(targetChannel3, channelElement, Boolean.parseBoolean(cmdArray[3]));

                }
                if (channelElement == Channel.channelElement.NAME) {
                    jcfChannel.updateChannel(targetChannel3, channelElement, cmdArray[3]);

                }
                if (channelElement == Channel.channelElement.DESCRIPTION) {
                    jcfChannel.updateChannel(targetChannel3, channelElement, cmdArray[3]);
                }
                break;
            case "readupdatedchannel":
                jcfChannel.readUpdatedChannel();
                break;
            case "readdeletedchannel":
                jcfChannel.readDeletedChannel();
                break;
            case "inviteusertochannel":
                if (cmdArray.length != 3) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                jcfChannel.inviteUserToChannel(targetUser(cmdArray[1]), targetChannel(cmdArray[2]));
                break;
            case "deleteuserfromchannel":
                if (cmdArray.length != 3) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                jcfChannel.deleteUserFromChannel(targetUser(cmdArray[1]), targetChannel(cmdArray[2]));
                break;
            case "createmessage":
                if (cmdArray.length != 4 || !checkValidateBoolean(cmdArray[3])) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                jcfMessage.createMessage(new Message(cmdArray[1], targetUser(cmdArray[2]), Boolean.parseBoolean(cmdArray[3])));
                break;
            case "readmessage":
                if (cmdArray.length != 2) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                jcfMessage.readMessage(targetMessage(cmdArray[1]));
                break;
            case "readallmessage":
                jcfMessage.readAllMessage();
                break;
            case "deletemessage":
                if (cmdArray.length != 2) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                jcfMessage.deleteMessage(targetMessage(cmdArray[1]));
                break;
            case "updatemessage":
                if (cmdArray.length != 4) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                Message targetMessage2 = targetMessage(cmdArray[1]);
                Message.messageElement messageElement;
                try {
                    messageElement = Message.messageElement.valueOf(cmdArray[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println(WRONG_INPUT);
                    break;
                }
                if (messageElement == Message.messageElement.CONTENT) {
                    jcfMessage.updateMessage(targetMessage2, messageElement, cmdArray[3]);
                }
                if (messageElement == Message.messageElement.IS_MARKDOWN) {
                    jcfMessage.updateMessage(targetMessage2, messageElement, Boolean.parseBoolean(cmdArray[3]));
                }
                break;
            case "readupdatedmessage":
                jcfMessage.readUpdatedMessage();
                break;
            case "readdeletedmessage":
                jcfMessage.readDeletedMessage();
                break;
            case "!help":
                if (cmdArray.length == 1) {
                    showHelp();
                    break;
                } else {
                    switch (cmdArray[1]) {
                        case "channel":
                            showChannelHelp();
                            break;
                        case "user":
                            showUserHelp();
                            break;
                        case "message":
                            showMessageHelp();
                            break;
                        default:
                            System.out.println("!help 혹은 !help <channel/user/message>를 입력하세요");
                    }
                    break;
                }
            default:
                System.out.println(WRONG_COMMAND);


        }
    }

    public void closeTest() {
        inputHandler.close();
        System.out.println("테스트 종료");
    }

    public boolean checkValidateBoolean(String input) {

        if (input == null) return false;
        String inputLowerCase = input.toLowerCase();
        return inputLowerCase.equals("true") || inputLowerCase.equals("false");
    }

    public Channel targetChannel(String channelName) {

        return jcfDb.getChannelDb()
                .stream()
                .filter(m -> m.getName().equals(channelName)).findFirst().orElse(null);
    }

    public User targetUser(String userName) {
        return jcfDb.getUserDb()
                .stream()
                .filter(m -> m.getName().equals(userName)).findFirst().orElse(null);
    }

    public Message targetMessage(String messageContent) {
        return jcfDb.getMessageDb()
                .stream()
                .filter(m -> m.getContent().equals(messageContent)).findFirst().orElse(null);
    }

    public void showHelp() {
        System.out.println("!help 명령어 : !help  또는 !help <channel/user/message>");
        System.out.println("\n ");
        showChannelHelp();
        System.out.print("\n\n");
        showMessageHelp();
        System.out.print("\n\n");
        showUserHelp();
    }

    public void showChannelHelp() {
        System.out.println("=========Channel 명령어 입니다==========");
        System.out.println("createchannel <channel_name> <description> <is_public> <is_text_channel>");
        System.out.println("readallchannel");
        System.out.println("deletechannel <channel_name>");
        System.out.println("readchannel <channel_name>");
        System.out.println("updatechannel <channel_name> <is_public> <is_text_channel>");
        System.out.println("readupdatedchannel");
        System.out.println("readdeletedchannel");
        System.out.println("inviteusertochannel <user_name> <channel_name>");
        System.out.println("deleteuserfromchannel <user_name> <channel_name>");
        System.out.println("=========================== ");
    }

    public void showUserHelp() {
        System.out.println("=========User 명령어 입니다==========");
        System.out.println("createuser <user_name> <password> <email> <is_online>");
        System.out.println("readalluser");
        System.out.println("deleteuser <user_name>");
        System.out.println("updateuser <user_name> <is_online>");
        System.out.println("readupdateduser");
        System.out.println("readdeleteduser");
        System.out.println("readuser <user_name>");
        System.out.println("enterchannel <user_name> <channel_name>");
        System.out.println("exitchannel <user_name> <channel_name>");
        System.out.println("=========================== ");


    }

    public void showMessageHelp() {
        System.out.println("=========Message 명령어 입니다==========");
        System.out.println("createmessage <user_name> <content> <is_markdown>");
        System.out.println("readmessage <message_content>");
        System.out.println("readallmessage");
        System.out.println("deletemessage <message_content>");
        System.out.println("updatemessage <message_content> <is_markdown>");
        System.out.println("readupdatedmessage");
        System.out.println("readdeletedmessage");
        System.out.println("=========================== ");

    }


}
