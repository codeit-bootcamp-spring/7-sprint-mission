package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.function.BiConsumer;

public class JCFChannel implements ChannelService {
private final ArrayList<Channel> channelDb;
private final  Map<UUID,String> deletedChannel ;
    private final JCFDb jcfDb;




    public JCFChannel(JCFDb jcfDb) {

        this.jcfDb = jcfDb;
this.channelDb = jcfDb.getChannelDb();
this.deletedChannel = jcfDb.getDeletedChannelDb();

    }


    public void addUserToChannel(User user, Channel channel){
        if(channelDb.stream().noneMatch(u->u.getId()==user.getId())){
            System.out.println("채널에 등록할 유저가 존재하지 않습니다.");
            return;
        }
        if(channelDb.stream().noneMatch(c->c.getId()==channel.getId())){
            System.out.println("채널이 존재하지 않습니다.");
            return;
        }
        channel.addUserToChannel(user);
    }

    public void deleteUserFromChannel(User user, Channel channel){
        if(channelDb.stream().noneMatch(u->u.getId()==user.getId())){
            System.out.println("채널에 유저가 존재하지 않습니다.");
            return;
        }
        if(channelDb.stream().noneMatch(c->c.getId()==channel.getId())){
            System.out.println("채널이 존재하지 않습니다.");
            return;
        }
        channel.removeUserFromChannel(user);
    }





    @Override
    public void createChannel(Channel channel) {
        jcfDb.createChannel(channel);
        System.out.printf("채널이 생성되었습니다 : %s\n", channel.getName());

    }

    @Override
    public void readChannel(Channel channel) {
        if(jcfDb.getDeletedChannelDb().containsKey(channel.getId())) {
            System.out.println(channel.getName() + ": 삭제된 채널입니다.");
            return;
        }
        if(jcfDb.getChannelDb().stream()
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
            if(channelDb.stream()
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
        for (Channel channel : channelDb) {
            readChannel(channel);
        }
    }

    @Override
    public void deleteChannel(Channel channel) {
        jcfDb.deleteChannel(channel);

    }

    @Override
    public <T> void updateChannel(Channel channel, Channel.channelElement channelElement, T updatedContent) {
        if(!checkIfChannelExist(channel)) return;

        BiConsumer<Channel, Object> editFunction = channelElement.setter;

//        Class<? extends BiConsumer> aClass = editFunction.getClass();
//        if(!aClass.isInstance(updatedContent)) {
//            System.out.println("잘못된 타입을 입력했습니다");
//            return;
//        }
        try
        {
            editFunction.accept(channel, updatedContent);
            channel.updateEntity();
            System.out.printf("채널 내부 정보를 변경했습니다.: %s\n", channel.getName());
        } catch (ClassCastException e) {

            System.out.println("잘못된 타입을 입력했습니다. 올바른 입력값을 넣어주세요");
        }


    }

    @Override
    public void readUpdatedChannel() {
        if (channelDb.stream().noneMatch(c -> c.getUpdatedAt() != Entity.DEFAULT_UPDATED_AT)) {
            System.out.println("업데이트 된 채널이 없습니다.");
            return;
        }
        for(Channel channel : channelDb){

            if(channel.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT){
                readChannel(channel);
                System.out.println(channel.getName()+" 변경 시간: "+" "+channel.getUpdatedAt() );
            }
        }
    }

    @Override
    public void readDeletedChannel() {
        if(deletedChannel.isEmpty()){
            System.out.println( "삭제된 채널이 없습니다.");
            return;
        }
        System.out.println( "===삭제된 채널=== ");
        for(UUID tmp :deletedChannel.keySet()){
            String value = deletedChannel.get(tmp);
            System.out.println(value);
        }
        System.out.println("==========");

    }
    public boolean checkIfChannelExist(Channel channel){
        if(channelDb.stream()
                .noneMatch(c->c.getId()==channel.getId())

        ){
            System.out.println("채널은 존재하지 않습니다. : "+ channel.getName());

            return false;
        }
        return true;

    }
    public void showUserInChannel(Channel channel){
        for(User user : channel.getUsers()){
            System.out.println(user.getName());
        }
    }


}
