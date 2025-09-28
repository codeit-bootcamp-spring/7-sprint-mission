package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import javax.security.auth.callback.CallbackHandler;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JCFChannel implements ChannelService {
    final ArrayList<Channel> channelDB;
    final ArrayList<String> deletedChannel = new ArrayList<>();
    public JCFChannel(ArrayList<Channel> channelDB) {
        this.channelDB = channelDB;
    }

    @Override
    public void createChannel(Channel channel) {
        channelDB.add(channel);
        System.out.printf("Channel created: %s\n", channel.getName());

    }

    @Override
    public void readChannel(Channel channel) {
        if(channelDB.stream()
                .noneMatch(c->c.getName().equals(channel.getName()))

        ){
            System.out.println("No such channel");
            return;
        }
        System.out.println("Channel info: "+ channel.toString() );

    }

    public void readChannel(Channel... channels) {

        for (Channel channel : channels) {
            if(channelDB.stream()
                    .noneMatch(c->c.getName().equals(channel.getName()))

            ){
                System.out.println("No such channel");
                continue;
            }
            System.out.println("Channel info: "+ channel.toString() );
        }
    }

    public void readAllChannel() {
        for (Channel channel : channelDB) {
            readChannel(channel);
        }
    }

    @Override
    public void deleteChannel(Channel channel) {
        if(channelDB.stream()
                .noneMatch(c->c.getName().equals(channel.getName()))

        ){
            System.out.println("No such channel");
            return;
        }
        deletedChannel.add(channel.getName());
        channelDB.remove(channel);
        System.out.printf("Channel deleted: %s\n", channel.getName());

    }

    @Override
    public <T> void updateChannel(Channel channel, Channel.channelElement channelElement, T updatedContent) {
        BiConsumer<Channel, Object> editFunction = channelElement.setter;

        editFunction.accept(channel, updatedContent);
        channel.updateEntity();
        System.out.printf("Channel updated: %s\n", channel.getName());

    }

    @Override
    public void readUpdatedChannel() {
        for(Channel channel : channelDB){

            if(channel.getUpdatedAt()!=0){
                readChannel(channel);
                System.out.println(channel.getName()+" is Updated at: "+" "+channel.getUpdatedAt() );
            }
        }
    }

    @Override
    public void readDeletedChannel() {
        System.out.println("Deleted Channel = " + deletedChannel);
    }
}
