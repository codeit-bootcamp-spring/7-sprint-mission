package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.etc.StaticString;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.etc.StaticString.*;

public class JCFChannel implements ChannelService {
private final ArrayList<Channel> channelDb;
private final  Map<UUID,String> deletedChannel ;
    private final JCFDb jcfDb;




    public JCFChannel(JCFDb jcfDb) {

        this.jcfDb = jcfDb;
this.channelDb = jcfDb.getChannelDb();
this.deletedChannel = jcfDb.getDeletedChannelDb();

    }


//    public void addUserToChannel(User user, Channel channel){
//        if(channelDb.stream().noneMatch(u->u.getId()==user.getId())){
//            System.out.println("채널에 등록할 유저가 존재하지 않습니다.");
//            return;
//        }
//        if(channelDb.stream().noneMatch(c->c.getId()==channel.getId())){
//            System.out.println("채널이 존재하지 않습니다.");
//            return;
//        }
//        channel.addUserToChannel(user);
//    }
//
//    public void deleteUserFromChannel(User user, Channel channel){
//        if(channelDb.stream().noneMatch(u->u.getId()==user.getId())){
//            System.out.println("채널에 유저가 존재하지 않습니다.");
//            return;
//        }
//        if(channelDb.stream().noneMatch(c->c.getId()==channel.getId())){
//            System.out.println("채널이 존재하지 않습니다.");
//            return;
//        }
//        channel.removeUserFromChannel(user);
//    }





    @Override
    public void createChannel(Channel channel) {
        jcfDb.createChannel(channel);
//        System.out.printf("채널이 생성되었습니다 : %s\n", channel.getName());

    }

    @Override
    public void readChannel(Channel channel) {
        if(jcfDb.getDeletedChannelDb().containsKey(channel.getId())) {
            System.out.println(CHANNEL_ALREADY_DELETED+ channel.getName() );
            return;
        }
        if(jcfDb.getChannelDb().stream()
                .noneMatch(c->c.getName().equals(channel.getName()))

        ){
            System.out.println(CHANNEL_NOT_EXIST+ channel.getName());
            return;
        }

        System.out.println("채널 정보: "+ channel.toString() );

    }

    public void readChannel(Channel... channels) {

        for (Channel channel : channels) {
            if(deletedChannel.containsKey(channel.getId())){
                System.out.println(CHANNEL_ALREADY_DELETED +channel.getName());
                continue;
            }
            if(channelDb.stream()
                    .noneMatch(c->c.getId()==channel.getId())

            ){
                System.out.println(CHANNEL_NOT_EXIST+ channel.getName());
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
        if(!jcfDb.isValidateChannel(channel)){
            System.out.println(CHANNEL_NOT_EXIST + channel.getName());
            return;
        }

        BiConsumer<Channel, Object> editFunction = channelElement.setter;
        Object oldContent = getChannelContent(channel,channelElement);

//        Class<? extends BiConsumer> aClass = editFunction.getClass();
//        if(!aClass.isInstance(updatedContent)) {
//            System.out.println("잘못된 타입을 입력했습니다");
//            return;
//        }
        try
        {
            System.out.printf("채널 내부 정보를 변경했습니다.: %s\n", channel.getName());
            System.out.println("변경한 필드: "+ channelElement.name()+ " 변경전: "+oldContent +" ==> 변경 후: "+updatedContent);
            editFunction.accept(channel, updatedContent);
            channel.updateEntity();

        } catch (ClassCastException e) {

            System.out.println(WRONG_TYPE);
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
//    public boolean checkIfChannelExist(Channel channel){
//        if(channelDb.stream()
//                .noneMatch(c->c.getId()==channel.getId())
//
//        ){
//            System.out.println("채널은 존재하지 않습니다. : "+ channel.getName());
//
//            return false;
//        }
//        return true;
//
//    }
    public void showUserInChannel(Channel channel){
        for(User user : channel.getUsers()){
            System.out.println(user.getName());
        }
    }
    public void inviteUserToChannel(User user, Channel channel){
        if(!jcfDb.isValidateChannel(channel)){
            System.out.println(CHANNEL_NOT_EXIST + channel.getName());
            return;
        }
        if(!jcfDb.isValidateUser(user)){
            System.out.println(USER_NOT_EXIST+ user.getName());
            return;
        }

        channel.addUserToChannel(user);
        user.addChannel(channel);
        System.out.println(user.getName()+"님이 "+channel.getName()+" 채널에 초대 됐습니다..");
    }
    public void deleteUserFromChannel(User user, Channel channel){
        if(!jcfDb.isValidateChannel(channel)){
            System.out.println(CHANNEL_NOT_EXIST + channel.getName());
            return;
        }
        if(!jcfDb.isValidateUser(user)){
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }

        if(!channel.getUserDb().contains(user)){
            System.out.println("채널에 등록된 유저가 아닙니다.");
            return;
        }
        System.out.println(user.getName()+"님이 "+channel.getName()+" 채널에서 추방되었습니다.");
        channel.removeUserFromChannel(user);
        user.removeChannel(channel);

    }

    public <T> Object getChannelContent(Channel channel, Channel.channelElement channelElement){
        switch(channelElement){
            case NAME:
                return channel.getName();

            case DESCRIPTION:
                return channel.getDescription();
            case IS_PUBLIC:
                return channel.isPublic();
                case IS_TEXT_CHANNEL:
                return channel.isTextChannel();
            default:
                return null;
        }
    }


}
