package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.util.StaticString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileReadStatusRepository implements ReadStatusRepository {



    private final String READ_STATUS_DATA_PATH;
    private final File readStatusRepositoryFile;



    public FileReadStatusRepository(Environment env) {
        READ_STATUS_DATA_PATH = env.getProperty("discodeit.repository.file-directory") +"readStatusRepository.ser";
        readStatusRepositoryFile = new File(READ_STATUS_DATA_PATH);

        repositoryCheck();
        resetRepository();
    }

    @Override
    public ReadStatus createReadStatus(ReadStatus readStatus) {
        Map<UUID, ReadStatus> readStatusMap = loadAllReadStatus();
        readStatusMap.put(readStatus.getId(),readStatus);
        saveAllReadStatus(readStatusMap);
        return readStatus;

    }

    @Override
    public void deleteReadStatus(UUID readStatusID) {
        Map<UUID, ReadStatus> readStatusMap = loadAllReadStatus();
        readStatusMap.remove(readStatusID);
        saveAllReadStatus(readStatusMap);

    }

    @Override
    public void updateReadStatus(ReadStatus readStatus) {

        Map<UUID, ReadStatus> readStatusMap = loadAllReadStatus();
        readStatusMap.remove(readStatus.getId());
        readStatusMap.put(readStatus.getId(),readStatus);
        saveAllReadStatus(readStatusMap);

    }

    @Override
    public Optional<ReadStatus> readReadStatus(UUID readStatusID) {
        return Optional.ofNullable(loadAllReadStatus().get(readStatusID));
    }

    @Override
    public List<ReadStatus> readAllReadStatus() {
        return loadAllReadStatus().values().stream().toList();
    }

    @Override
    public boolean isReadStatusExist(UUID userId, UUID channelId) {
        return loadAllReadStatus().containsKey(userId);
    }
    private void saveAllReadStatus(Map<UUID, ReadStatus> readStatusMap){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(readStatusRepositoryFile,false));){
            oos.writeObject(readStatusMap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Map<UUID,ReadStatus> loadAllReadStatus(){
        if(!readStatusRepositoryFile.exists() || readStatusRepositoryFile.length() == 0) {
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(readStatusRepositoryFile));){
            return (Map<UUID, ReadStatus>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();

    }

    private void repositoryCheck(){
        if(!readStatusRepositoryFile.exists()){
            try{
                readStatusRepositoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
    @Override
    public void resetRepository(){
        saveAllReadStatus(new HashMap<>());
    }
}
