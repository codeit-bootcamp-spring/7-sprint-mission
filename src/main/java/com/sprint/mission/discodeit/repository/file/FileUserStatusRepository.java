package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.util.StaticString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
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
public class FileUserStatusRepository implements UserStatusRepository {


    private final String USER_STATUS_DATA_PATH;
    private final File userStatusRepositoryFile;

    public FileUserStatusRepository(Environment env) {
        USER_STATUS_DATA_PATH =env.getProperty(DISCODEIT_DIRECTORY)+"userStatusRepository.ser";
        userStatusRepositoryFile = new File(USER_STATUS_DATA_PATH);
        repositoryCheck();
    }

    @Override
    public UserStatus createUserStatus(UserStatus userStatus) {
        Map<UUID,UserStatus> userStatusMap = loadAllUserStatus();
        userStatusMap.put(userStatus.getId(),userStatus);
        saveAllUserStatus(userStatusMap);
        return userStatus;
    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        Map<UUID,UserStatus> userStatusMap = loadAllUserStatus();
        userStatusMap.remove(userStatusId);
        saveAllUserStatus(userStatusMap);

    }

    @Override
    public void updateUserStatus(UserStatus userStatus) {
        Map<UUID,UserStatus> userStatusMap = loadAllUserStatus();
        userStatusMap.put(userStatus.getId(),userStatus);
        saveAllUserStatus(userStatusMap);

    }

    @Override
    public Optional<UserStatus> readUserStatus(UUID userStatusId) {
        return Optional.ofNullable(loadAllUserStatus().get(userStatusId));
    }

    @Override
    public List<UserStatus> readAllUserStatus() {
        return loadAllUserStatus().values().stream().toList();
    }

    @Override
    public boolean isUserStatusExist(UUID userStatusId) {
        return loadAllUserStatus().containsKey(userStatusId);
    }

    private void saveAllUserStatus(Map<UUID,UserStatus> userStatusList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userStatusRepositoryFile,false));){
            oos.writeObject(userStatusList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Map<UUID,UserStatus> loadAllUserStatus(){
        if(!userStatusRepositoryFile.exists() || userStatusRepositoryFile.length() == 0) {
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userStatusRepositoryFile))){
            return ( Map<UUID,UserStatus>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }
    private void repositoryCheck(){
        if(!userStatusRepositoryFile.exists()){
            try{
                userStatusRepositoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void resetRepository(){
        saveAllUserStatus(new HashMap<>());
    }

}
