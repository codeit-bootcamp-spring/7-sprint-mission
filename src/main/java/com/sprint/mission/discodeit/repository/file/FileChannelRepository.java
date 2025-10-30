package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

import static com.sprint.mission.discodeit.service.util.StaticString.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = false
)
public class FileChannelRepository implements ChannelRepository {


    private final String channelRepositoryDataPath;
    private final File channelRepositoryFile;

    public FileChannelRepository(Environment env) {
        channelRepositoryDataPath = env.getProperty("discodeit.repository.file-directory")+"channelRepository.ser";
        System.out.println(channelRepositoryDataPath);
        channelRepositoryFile = new File(channelRepositoryDataPath);
        repositoryCheck();
        resetChannelRepository();
    }
    @Override
    public Optional<Channel> getChannelById(UUID channelId) {

        return Optional.ofNullable(loadAllChannel().get(channelId));
    }

    @Override
    public Optional<Channel> getChannelByName(String channelName) {

        return loadAllChannel().values().stream().filter(x->x.getName().equals(channelName)).findFirst();
    }

    public Optional<Channel> getChannel(Channel channel){

        return getChannelById(channel.getId());
    }

    @Override
    public void deleteChannel(Channel channel) {
        Map<UUID,Channel> channelMap = loadAllChannel();
       channelMap.remove(channel.getId());
       saveAllChannel(channelMap);

    }

    @Override
    public <T>void updateChannel(Channel channel) {
        deleteChannel(channel);
        saveChannel(channel);
    }




    @Override
    public List<Channel> getAllChannel() {
        return loadAllChannel().values().stream().toList();

    }

    @Override
    public List<Channel> getUpdatedChannel() {
        Map<UUID,Channel> channels = loadAllChannel();
        return channels.values().stream().filter(x->x.getUpdatedAt()!= x.getCreatedAt()).toList();

    }

//    @Override
//    public DeletedChannel[] getDeletedChannel() {
//        return loadAllDeletedChannel().stream().map(this::deletedChannelToDeletedChannelDto).toArray(DeletedChannelDto[]::new);
//    }

    private void repositoryCheck(){
        if(!channelRepositoryFile.exists()){
            try{
                channelRepositoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resetChannelRepository() {
        saveAllChannel(new HashMap<>());
    }

    @Override
    public boolean isChannelExit(UUID channelId) {
        return loadAllChannel().containsKey(channelId);
    }


    private void saveAllChannel(Map<UUID,Channel>channelMap){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(channelRepositoryFile,false));){
            oos.writeObject(channelMap);
            oos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private Map<UUID,Channel> loadAllChannel(){
        if(!channelRepositoryFile.exists() || channelRepositoryFile.length() == 0) {
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(channelRepositoryFile));){
            return (Map<UUID, Channel>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

//    private List<DeletedChannel> loadAllDeletedChannel(){
//        if(!deletedChannelRepositoryFile.exists() || deletedChannelRepositoryFile.length() == 0) {
//            return new ArrayList<>();
//        }
//        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(deletedChannelRepositoryDataPath));){
//            return (List<DeletedChannel>) ois.readObject();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }
//
//    private void saveAllDeletedChannel(List<DeletedChannel>deletedChannelList){
//        try (
//                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(deletedChannelRepositoryFile))){
//            oos.writeObject(deletedChannelList);
//            oos.flush();
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }


    @Override
    public Channel saveChannel(Channel channel){

        Map<UUID,Channel> channels = loadAllChannel();
        channels.put(channel.getId(),channel);
        saveAllChannel(channels);
        return channel;
    }


}
