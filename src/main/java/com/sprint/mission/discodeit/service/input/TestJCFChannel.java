package com.sprint.mission.discodeit.service.input;

public class TestJCFChannel {

//    private final TestUtil testUtil;
////    private final JCFChannel jcfChannel ;
//    private final ChannelService channelService;
//
//
//
//    public TestJCFChannel(TestUtil testUtil, ChannelService channelService) {
//        this.testUtil = testUtil;
//        this.channelService = channelService;
//    }
//
//    void testCreateChannel(String [] cmdArray) {
//        if (cmdArray.length != 5 || !testUtil.checkValidateBoolean(cmdArray[3]) || !testUtil.checkValidateBoolean(cmdArray[4])) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        channelService.createChannel(new Channel(cmdArray[1], cmdArray[2], Boolean.parseBoolean(cmdArray[3]), Boolean.parseBoolean(cmdArray[4])));
//    }
//    void testDeleteChannel(String[] cmdArray) {
//        if (cmdArray.length != 2) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        channelService.deleteChannel(testUtil.targetChannel(cmdArray[1]));
//    }
//    void testUpdateChannel(String[] cmdArray) {
//        if (cmdArray.length != 4) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        Channel targetChannel3 = testUtil.targetChannel(cmdArray[1]);
//
//        Channel.channelElement channelElement;
//        try {
//            channelElement = Channel.channelElement.valueOf(cmdArray[2].toUpperCase());
//        } catch (IllegalArgumentException e) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//
//        if (channelElement == Channel.channelElement.IS_PUBLIC) {
//            channelService.updateChannel(targetChannel3, channelElement, Boolean.parseBoolean(cmdArray[3]));
//
//        }
//        if (channelElement == Channel.channelElement.IS_TEXT_CHANNEL) {
//            channelService.updateChannel(targetChannel3, channelElement, Boolean.parseBoolean(cmdArray[3]));
//
//        }
//        if (channelElement == Channel.channelElement.NAME) {
//            channelService.updateChannel(targetChannel3, channelElement, cmdArray[3]);
//
//        }
//        if (channelElement == Channel.channelElement.DESCRIPTION) {
//            channelService.updateChannel(targetChannel3, channelElement, cmdArray[3]);
//        }
//        return;
//    }
//    void testReadChannel(String [] cmdArray) {
//        if (cmdArray.length != 2) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        channelService.readChannel(testUtil.targetChannel(cmdArray[1]));
//
//
//    }
//    void testReadAllChannel()
//        {channelService.readAllChannel();}
//    void testInviteUserToChannel(String[] cmdArray) {
//        if (cmdArray.length != 3) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        channelService.inviteUserToChannel(testUtil.targetUser(cmdArray[1]), testUtil.targetChannel(cmdArray[2]));
//        return;
//    }
//    void testDeleteUserFromChannel(String[] cmdArray) {
//        if (cmdArray.length != 3) {
//            System.out.println(WRONG_INPUT);
//            return;
//        }
//        channelService.deleteUserFromChannel(testUtil.targetUser(cmdArray[1]), testUtil.targetChannel(cmdArray[2]));
//        return;
//
//    }
//    void testReadUpdatedChannel() {
//
//        channelService.readUpdatedChannel();
//    }
//    void testReadDeletedChannel() {
//        channelService.readDeletedChannel();
//
//    }
}
