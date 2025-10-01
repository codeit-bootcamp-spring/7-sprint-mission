package com.sprint.mission.discodeit.service.input;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;

import static com.sprint.mission.discodeit.static_.StaticString.WRONG_INPUT;

public class TestJCFChannel {

    private final TestUtil testUtil;
    private final JCFChannel jcfChannel ;


    public TestJCFChannel(TestUtil testUtil, JCFChannel jcfChannel) {
        this.testUtil = testUtil;
        this.jcfChannel = jcfChannel;
    }

    void testCreateChannel(String [] cmdArray) {
        if (cmdArray.length != 5 || !testUtil.checkValidateBoolean(cmdArray[3]) || !testUtil.checkValidateBoolean(cmdArray[4])) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfChannel.createChannel(new Channel(cmdArray[1], cmdArray[2], Boolean.parseBoolean(cmdArray[3]), Boolean.parseBoolean(cmdArray[4])));
    }
    void testDeleteChannel(String[] cmdArray) {
        if (cmdArray.length != 2) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfChannel.deleteChannel(testUtil.targetChannel(cmdArray[1]));
    }
    void testUpdateChannel(String[] cmdArray) {
        if (cmdArray.length != 4) {
            System.out.println(WRONG_INPUT);
            return;
        }
        Channel targetChannel3 = testUtil.targetChannel(cmdArray[1]);

        Channel.channelElement channelElement;
        try {
            channelElement = Channel.channelElement.valueOf(cmdArray[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(WRONG_INPUT);
            return;
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
        return;
    }
    void testReadChannel(String [] cmdArray) {
        if (cmdArray.length != 2) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfChannel.readChannel(testUtil.targetChannel(cmdArray[1]));


    }
    void testReadAllChannel()
        {jcfChannel.readAllChannel();}
    void testInviteUserToChannel(String[] cmdArray) {
        if (cmdArray.length != 3) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfChannel.inviteUserToChannel(testUtil.targetUser(cmdArray[1]), testUtil.targetChannel(cmdArray[2]));
        return;
    }
    void testDeleteUserFromChannel(String[] cmdArray) {
        if (cmdArray.length != 3) {
            System.out.println(WRONG_INPUT);
            return;
        }
        jcfChannel.deleteUserFromChannel(testUtil.targetUser(cmdArray[1]), testUtil.targetChannel(cmdArray[2]));
        return;

    }
    void testReadUpdatedChannel() {

        jcfChannel.readUpdatedChannel();
    }
    void testReadDeletedChannel() {
        jcfChannel.readDeletedChannel();

    }
}
