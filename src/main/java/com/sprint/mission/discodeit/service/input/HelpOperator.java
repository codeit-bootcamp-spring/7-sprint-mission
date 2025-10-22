package com.sprint.mission.discodeit.service.input;

public class HelpOperator {


    public void showHelp() {
        System.out.println("!help command : !help  or !help <channel/user/message>");
        System.out.println("\n ");
        showChannelHelp();
        System.out.print("\n\n");
        showMessageHelp();
        System.out.print("\n\n");
        showUserHelp();
        System.out.println("\n !exit");
    }

    public void showChannelHelp() {
        System.out.println("=========Channel Command==========");
        System.out.println("createchannel <channel_name> <description> <is_public> <is_text_channel>");
        System.out.println("readallchannel");
        System.out.println("deletechannel <channel_name>");
        System.out.println("readchannel <channel_name>");
        System.out.println("updatechannel <channel_name> <channel_element> <update content>>");
        System.out.println("readupdatedchannel");
        System.out.println("readdeletedchannel");
        System.out.println("inviteusertochannel <user_name> <channel_name>");
        System.out.println("deleteuserfromchannel <user_name> <channel_name>");
        System.out.println("=========================== ");
    }

    public void showUserHelp() {
        System.out.println("=========User Command==========");
        System.out.println("createuser <user_name> <nickname> <email> <is_online>");
        System.out.println("readalluser");
        System.out.println("deleteuser <user_name>");
        System.out.println("updateuser <user_name> <user_element> <update content  >");
        System.out.println("readupdateduser");
        System.out.println("readdeleteduser");
        System.out.println("readuser <user_name>");
        System.out.println("enterchannel <user_name> <channel_name>");
        System.out.println("exitchannel <user_name> <channel_name>");
        System.out.println("=========================== ");


    }

    public void showMessageHelp() {
        System.out.println("=========Message Command==========");
        System.out.println("createmessage <content> <user_name> <is_markdown>");
        System.out.println("readmessage <message_content>");
        System.out.println("readallmessage");
        System.out.println("deletemessage <message_content>");
        System.out.println("updatemessage <message_content> <message_element> <update content>");
        System.out.println("readupdatedmessage");
        System.out.println("readdeletedmessage");
        System.out.println("=========================== ");

    }

    public void helpFinal(String[] cmdArray) {
        if (cmdArray.length == 1) {
            showHelp();
            return;
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
                    System.out.println("!help or !help <channel/user/message> ");
            }
        }
    }
}
