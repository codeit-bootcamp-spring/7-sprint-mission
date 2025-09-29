package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.service.ChannelService;

import javax.security.auth.callback.CallbackHandler;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class JCFChannel implements ChannelService {
    final ArrayList<Channel> channelDB;
    final  Map<UUID,String> deletedChannel =new HashMap<>();
    public JCFChannel(ArrayList<Channel> channelDB) {
        this.channelDB = channelDB;
    }
    public JCFChannel() {
        this.channelDB = new ArrayList<>();
    }

    @Override
    public void createChannel(Channel channel) {
        channelDB.add(channel);
        System.out.printf("채널이 생성되었습니다 : %s\n", channel.getName());

    }

    @Override
    public void readChannel(Channel channel) {
        if(deletedChannel.containsKey(channel.getId())) {
            System.out.println(channel.getName() + ": 삭제된 채널입니다.");
            return;
        }
        if(channelDB.stream()
                .noneMatch(c->c.getName().equals(channel.getName()))

        ){
            System.out.println("채널은 존재하지 않습니다. : "+ channel.getName());
            return;
        }

        System.out.println("채널 정보: "+ channel.toString() );

    }

    public void readChannel(Channel... channels) {

        for (Channel channel : channels) {
            if(deletedChannel.containsKey(channel.getId())){
                System.out.println(channel.getName()+": 삭제된 채널입니다.");
                continue;
            }
            if(channelDB.stream()
                    .noneMatch(c->c.getId()==channel.getId())

            ){
                System.out.println("채널은 존재하지 않습니다. : "+ channel.getName());
                continue;
            }

            System.out.println("채널 정보: "+ channel.toString() );
        }
    }
    @Override
    public void readAllChannel() {
        for (Channel channel : channelDB) {
            readChannel(channel);
        }
    }

    @Override
    public void deleteChannel(Channel channel) {
        if(channelDB.stream()
                .noneMatch(c->c.getId()==channel.getId())

        ){
            System.out.println("No such channel");
            return;
        }
        deletedChannel.put(channel.getId(),channel.getName());
        channelDB.remove(channel);
        System.out.printf("채널을 삭제합니다: %s\n", channel.getName());

    }

    @Override
    public <T> void updateChannel(Channel channel, Channel.channelElement channelElement, T updatedContent) {
        BiConsumer<Channel, Object> editFunction = channelElement.setter;
        Class<? extends BiConsumer> aClass = editFunction.getClass();
        if(!aClass.isInstance(updatedContent)) {
            System.out.println("잘못된 타입을 입력했습니다");
            return;
        }
        editFunction.accept(channel, updatedContent);
        channel.updateEntity();
        System.out.printf("채널 내부 정보를 변경했습니다.: %s\n", channel.getName());

    }

    @Override
    public void readUpdatedChannel() {
        if (channelDB.stream().noneMatch(c -> c.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT)) {
            System.out.println("업데이트 된 채널이 없습니다.");
            return;
        }
        for(Channel channel : channelDB){

            if(channel.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT){
                readChannel(channel);
                System.out.println(channel.getName()+" 변경 시간: "+" "+channel.getUpdatedAt() );
            }
        }
    }

    @Override
    public void readDeletedChannel() {
        System.out.println( "===삭제된 채널=== ");
        for(UUID tmp :deletedChannel.keySet()){
            String value = deletedChannel.get(tmp);
            System.out.println(value);
        }

    }
}
