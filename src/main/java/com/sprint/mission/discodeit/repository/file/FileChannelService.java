package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.DeletedChannel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.util.AppendableObjectOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelRepository {


    private final String DataRootPath = "C:\\Users\\황준영\\Java-codeit\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\data\\";
    private final String channelRepositoryDataPath = DataRootPath+"channelRepository.ser";
    private final String deletedChannelRepositoryDataPath = DataRootPath+"deletedChannelRepository.ser";
    private File channelRepositoryFile = new File(channelRepositoryDataPath);
    private File deletedChannelRepositoryFile = new File(deletedChannelRepositoryDataPath);

    public FileChannelService() {
        repositoryCheck();
        resetChannelRepository();
    }
    @Override
    public ChannelDto getChannelById(UUID channelId) {
        return loadAllChannel().stream().filter(x->x.getId().equals(channelId)).map(this::channelToChannelDto).findFirst().orElse(null);
    }

    @Override
    public ChannelDto getChannelByName(String channelName) {
        return loadAllChannel().stream().filter(x->x.getName().equals(channelName)).map(this::channelToChannelDto).findFirst().orElse(null);
    }

    public ChannelDto  getChannel(ChannelDto channelDto){
        return getChannelById(channelDto.getId());
    }


//    public void saveChannel(ChannelDto channelDto) {
//        boolean isExist = channelRepositoryFile.exists() && channelRepositoryFile.length()>0;
//        try(ObjectOutputStream oos =
//                isExist ? new AppendableObjectOutputStream(new FileOutputStream(channelRepositoryDataPath,true))
//                : new ObjectOutputStream(new FileOutputStream(channelRepositoryDataPath))){
//         oos.writeObject(channelDtoToChannel(channelDto));
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void deleteChannel(ChannelDto channelDto) {
      List<Channel> channels = loadAllChannel();
      List<DeletedChannel> deletedChannels = loadAllDeletedChannel();
      channels.removeIf(x->x.getId().equals(channelDto.getId()));
      deletedChannels.add(channelDtoToDeletedChannel(channelDto));
      saveAllChannel(channels);
      saveAllDeletedChannel(deletedChannels);

    }

    @Override
    public <T>void updateChannel(ChannelDto channelDto, Channel.channelElement channelElement, T updatedContent) {
         List<Channel> channels = loadAllChannel();
         Channel targetChannel = channels.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().orElse(null);
         BiConsumer<Channel, Object> editFunction = channelElement.setter;
         editFunction.accept(targetChannel, updatedContent);
         targetChannel.updateEntity();
         saveAllChannel(channels);
//        try(
//                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(channelRepositoryDataPath));
//                       )
//        {
//
//            Channel updatedChannel = channelDtoToChannel(channelDto);
//            BiConsumer<Channel, Object> editFunction = channelElement.setter;
//            Object oldContent = channelElement.getter.apply(updatedChannel);
//
//            editFunction.accept(updatedChannel, updatedContent);
//            updatedChannel.updateEntity();
//
//
//            List<Channel>tempChannels = new ArrayList<>();
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(channelRepositoryDataPath));
//            while(true){
//                try{
//                    Channel tempChannel = (Channel) ois.readObject();
//                    tempChannels.add(tempChannel);
//                }
//                catch (EOFException e){
//                    break;
//                }
//            }
//            ois.close();
//            Channel previousChannel = tempChannels.stream().filter(x->x.getId()==channelDto.getId()).findFirst().get();
//            tempChannels.remove(previousChannel);
//            tempChannels.add(updatedChannel);
//
//            oos.writeObject(tempChannels);
        }




    @Override
    public ChannelDto[] getAllChannel() {
       return loadAllChannel().stream().map(this::channelToChannelDto).toArray(ChannelDto[]::new);

    }

    @Override
    public ChannelDto[] getUpdatedChannel() {
        List<Channel> channels = loadAllChannel();
        return channels.stream().filter(x->x.getUpdatedAt()!=Entity.DEFAULT_UPDATED_AT).map(this::channelToChannelDto).toArray(ChannelDto[]::new);

    }

    @Override
    public DeletedChannelDto[] getDeletedChannel() {
       return loadAllDeletedChannel().stream().map(this::deletedChannelToDeletedChannelDto).toArray(DeletedChannelDto[]::new);
    }

    private void repositoryCheck(){
        if(!channelRepositoryFile.exists()){
            try{
                channelRepositoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!deletedChannelRepositoryFile.exists()){
            try{
                deletedChannelRepositoryFile.createNewFile();
            } catch (IOException e) {}
        }


    }


    @Override
    public void addUserToChannel(UserDto userDto, ChannelDto channelDto) {
        List<Channel> channelDb = loadAllChannel();
        Channel targetChannel = channelDb.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().orElse(null);
        User tempUser = new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
        targetChannel.addUserToChannel(tempUser);
        saveAllChannel(channelDb);

    }

    @Override
    public void deleteUserFromChannel(UserDto userDto, ChannelDto channelDto) {

        if(userDto==null || channelDto==null){
            return;
        }
        List<Channel> channelDb = loadAllChannel();
        Channel targetChannel = channelDb.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().orElse(null);
        User tempUser = new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
        targetChannel.removeUserFromChannel(tempUser);
        saveAllChannel(channelDb);

//        try(
//                ObjectInputStream ois = new ObjectInputStream
//                        (new FileInputStream
//                                (channelRepositoryDataPath));
//        )
//        {
//
//            ObjectOutputStream oosChannel = new ObjectOutputStream
//                    (new FileOutputStream(channelRepositoryDataPath));
//
//            List<Channel> channelDb = (List<Channel>) ois.readObject();
//
//
//            Channel tempChannel = channelDb.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().get();
//            User tempUser = new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
//            tempChannel.removeUserFromChannel(tempUser);
//            channelDb.remove(channelDtoToChannel(channelDto));
//            channelDb.add(tempChannel);
//
//            oosChannel.writeObject(channelDb);
//            oosChannel.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

    }

    @Override
    public void resetChannelRepository() {
        saveAllChannel(new ArrayList<>());
        saveAllDeletedChannel(new ArrayList<>());
    }

    private ChannelDto channelToChannelDto(Channel channel){
        List<UserDto> userDtoList = channel.getUserDb().stream().map(
                x->new UserDto(x.getId(),x.getName(),x.getNickname(),x.getEmail(),x.isOnline())
        ).collect(Collectors.toList());

        return new ChannelDto(channel.getId(),channel.getName(),channel.getDescription(),channel.isPublic(),channel.isTextChannel(),userDtoList);
    }
    private Channel channelDtoToChannel(ChannelDto channelDto){
        List<User> userDb = channelDto.getUserDtoList().stream().map(
                x->new User(x.getId(),x.getName(),x.getNickname(),x.getEmail(),x.isOnline())
        ).collect(Collectors.toList());
        return new Channel(channelDto.getId(),channelDto.getName(),channelDto.getDescription(),channelDto.isPublic(),channelDto.isTextChannel(),userDb);
    }
    private DeletedChannelDto deletedChannelToDeletedChannelDto(DeletedChannel deletedChannel){
        return new DeletedChannelDto(deletedChannel.getName());
    }
    private DeletedChannel deletedChannelDtoToDeletedChannel(DeletedChannelDto deletedChannelDto){
        return new DeletedChannel(deletedChannelDto.getName());
    }

    private DeletedChannel channelDtoToDeletedChannel(ChannelDto channelDto){
        return new DeletedChannel(channelDto.getName());
    }

    private void saveAllChannel(List<Channel>channelList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(channelRepositoryDataPath,false));){
            oos.writeObject(channelList);
            oos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private List<Channel> loadAllChannel(){
        if(!channelRepositoryFile.exists() || channelRepositoryFile.length() == 0) {
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(channelRepositoryDataPath));){
            return (List<Channel>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<DeletedChannel> loadAllDeletedChannel(){
        if(!deletedChannelRepositoryFile.exists() || deletedChannelRepositoryFile.length() == 0) {
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(deletedChannelRepositoryDataPath));){
            return (List<DeletedChannel>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void saveAllDeletedChannel(List<DeletedChannel>deletedChannelList){
        try (
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(deletedChannelRepositoryFile))){
            oos.writeObject(deletedChannelList);
            oos.flush();

            }
            catch (Exception e){
                e.printStackTrace();
            }

    }
    @Override
    public void saveChannel(ChannelDto channelDto){

        List<Channel> channels = loadAllChannel();
        channels.add(channelDtoToChannel(channelDto));
        saveAllChannel(channels);
        return;
    }



}
