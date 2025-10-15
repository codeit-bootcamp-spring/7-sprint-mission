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

public class FileChannelService implements ChannelRepository {


    private final String DataRootPath = "C:\\Users\\황준영\\Java-codeit\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\data\\";
    private final String channelRepositoryDataPath = DataRootPath+"channelRepository.ser";
    private final String deletedChannelRepositoryDataPath = DataRootPath+"deletedChannelRepository.ser";
    private File channelRepositoryFile = new File(channelRepositoryDataPath);
    private File deletedChannelRepositoryFile = new File(deletedChannelRepositoryDataPath);

    public FileChannelService() {
        repositoryCheck();
    }
    @Override
    public ChannelDto getChannelById(UUID channelId) {
        try(ObjectInputStream ois
                = new ObjectInputStream(
                        new FileInputStream(channelRepositoryDataPath)
        )){

            List<Channel> channelDb = (List<Channel>) ois.readObject();
            boolean isExist = channelDb.stream().anyMatch(x -> x.getId() == channelId);
            if(!isExist) return null;

            return channelDb.stream().filter(x->x.getId()==channelId).findFirst()
                    .map(this::channelToChannelDto).orElse(null);
        }
            catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ChannelDto getChannelByName(String channelName) {
        try(ObjectInputStream ois
                    = new ObjectInputStream(
                new FileInputStream(channelRepositoryFile)
        )){

            List<Channel> channelDb = (List<Channel>) ois.readObject();
            boolean isExist = channelDb.stream().anyMatch(x -> x.getName() == channelName);
            if(!isExist) return null;
            return channelDb.stream().filter(x->x.getName()==channelName).findFirst()
                    .map(this::channelToChannelDto).orElse(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        boolean isExist = deletedChannelRepositoryFile.exists() && deletedChannelRepositoryFile.length()>0;

        try(ObjectOutputStream oos =
                isExist ? new AppendableObjectOutputStream(new FileOutputStream(deletedChannelRepositoryDataPath,true))
                        : new ObjectOutputStream(new FileOutputStream(deletedChannelRepositoryDataPath));
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(channelRepositoryDataPath));

                ){
            oos.writeObject(channelDtoToDeletedChannel(channelDto));
            List<Channel> tempChannelList = (List<Channel>) ois.readObject();
            tempChannelList.removeIf(x->x.getId()==channelDto.getId());
            ois.close();
            ObjectOutputStream oosChannel = new ObjectOutputStream(new FileOutputStream(channelRepositoryDataPath));
            oosChannel.writeObject(tempChannelList);
            oosChannel.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public <T>void updateChannel(ChannelDto channelDto, Channel.channelElement channelElement, T updatedContent) {
        try(
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(channelRepositoryDataPath));
                       )
        {

            Channel updatedChannel = channelDtoToChannel(channelDto);
            BiConsumer<Channel, Object> editFunction = channelElement.setter;
            Object oldContent = channelElement.getter.apply(updatedChannel);

            editFunction.accept(updatedChannel, updatedContent);
            updatedChannel.updateEntity();


            List<Channel>tempChannels = new ArrayList<>();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(channelRepositoryDataPath));
            while(true){
                try{
                    Channel tempChannel = (Channel) ois.readObject();
                    tempChannels.add(tempChannel);
                }
                catch (EOFException e){
                    break;
                }
            }
            ois.close();
            Channel previousChannel = tempChannels.stream().filter(x->x.getId()==channelDto.getId()).findFirst().get();
            tempChannels.remove(previousChannel);
            tempChannels.add(updatedChannel);

            oos.writeObject(tempChannels);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public ChannelDto[] getAllChannel() {
        try(ObjectInputStream ois
                    = new ObjectInputStream(
                new FileInputStream(channelRepositoryDataPath)
        )){
            List<Channel> channelDb = (List<Channel>) ois.readObject();
            return channelDb.stream().map(this::channelToChannelDto).toArray(ChannelDto[]::new);




        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public ChannelDto[] getUpdatedChannel() {
        try(ObjectInputStream ois
                = new ObjectInputStream(
                        new FileInputStream(channelRepositoryDataPath))
        ){
            List<Channel> channelDb = (List<Channel>) ois.readObject();
            return channelDb.stream().filter(x->x.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT).map(this::channelToChannelDto).toArray(ChannelDto[]::new);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public DeletedChannelDto[] getDeletedChannel() {
        try(ObjectInputStream ois
                = new ObjectInputStream(
                        new FileInputStream(deletedChannelRepositoryDataPath))
        ){
            List<DeletedChannel> deletedChannelDb = (List<DeletedChannel>) ois.readObject();
            return deletedChannelDb.stream().map(this::deletedChannelToDeletedChannelDto).toArray(DeletedChannelDto[]::new);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return null;
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
        if(userDto==null || channelDto==null){
            return;
        }
        try(
                ObjectInputStream ois = new ObjectInputStream
                        (new FileInputStream
                                (channelRepositoryDataPath));)

        {

            List<Channel> channelDb = (List<Channel>) ois.readObject();

            ois.close();

            ObjectOutputStream oosChannel = new ObjectOutputStream
                    (new FileOutputStream(channelRepositoryDataPath));

            Channel tempChannel = channelDb.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().get();
            User tempUser = new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
            tempChannel.addUserToChannel(tempUser);
            channelDb.remove(channelDtoToChannel(channelDto));
            channelDb.add(tempChannel);

            oosChannel.writeObject(channelDb);
            oosChannel.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void deleteUserFromChannel(UserDto userDto, ChannelDto channelDto) {

        if(userDto==null || channelDto==null){
            return;
        }
        try(
                ObjectInputStream ois = new ObjectInputStream
                        (new FileInputStream
                                (channelRepositoryDataPath));
        )
        {

            ObjectOutputStream oosChannel = new ObjectOutputStream
                    (new FileOutputStream(channelRepositoryDataPath));

            List<Channel> channelDb = (List<Channel>) ois.readObject();


            Channel tempChannel = channelDb.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().get();
            User tempUser = new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
            tempChannel.removeUserFromChannel(tempUser);
            channelDb.remove(channelDtoToChannel(channelDto));
            channelDb.add(tempChannel);

            oosChannel.writeObject(channelDb);
            oosChannel.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private ChannelDto channelToChannelDto(Channel channel){
        List<UserDto> userDtoList = channel.getUserDb().stream().map(
                x->new UserDto(x.getId(),x.getName(),x.getNickname(),x.getEmail(),x.isOnline())
        ).toList();

        return new ChannelDto(channel.getName(),channel.getDescription(),channel.isPublic(),channel.isTextChannel(),userDtoList);
    }
    private Channel channelDtoToChannel(ChannelDto channelDto){
        List<User> userDb = channelDto.getUserDtoList().stream().map(
                x->new User(x.getId(),x.getName(),x.getNickname(),x.getEmail(),x.isOnline())
        ).toList();
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
    @Override
    public void saveChannel(ChannelDto channelDto){

        List<Channel> channels = loadAllChannel();
        channels.add(channelDtoToChannel(channelDto));
        saveAllChannel(channels);
        return;
    }



}
