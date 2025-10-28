package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
        matchIfMissing = true
)
public class FileUserRepository implements UserRepository {

    @Override
    public boolean isUserExit(UUID userId) {
        return loadAllUser().containsKey(userId);
    }
    @Override
    public void resetUserRepository() {
        saveAllUser(new HashMap<>());
    }

    @Value("${discodeit.repository.file-directory}")
    private final String USER_DATA_ROOT ;
    private final File userRepositoryFile;

    public FileUserRepository(Environment env) {
        USER_DATA_ROOT = env.getProperty("discodeit.repository.file-directory")+"userRepository.ser";
        System.out.println("USER_DATA_ROOT = " + USER_DATA_ROOT);
        userRepositoryFile = new File(USER_DATA_ROOT);
        repositoryFileCheck();
        resetUserRepository();
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return Optional.ofNullable(loadAllUser().get(userId));

    }

    @Override
    public Optional<User> getUser(User user) {

        return getUserById(user.getId());
    }

    @Override
    public Optional<User> getUserByName(String userName) {
        return loadAllUser().values().stream().filter(x->x.getName().equals(userName)).findFirst();
    }

    @Override
    public List<User> getAllUser() {
        return
                loadAllUser().values().stream().toList();
    }

    @Override
    public User saveUser(User user) {
        Map<UUID,User> userDb = loadAllUser();
        userDb.put(user.getId(),user);
        saveAllUser(userDb);
        return user;

    }

    @Override
    public void deleteUser(UUID userId) {

        Map<UUID,User> userDb = loadAllUser();
        userDb.remove(userId);
        saveAllUser(userDb);
        return;
    }

    @Override
    public void updateUser(User user) {
        deleteUser(user.getId());
        saveUser(user);


    }

    @Override
    public List<User> getUpdatedUser() {
        return loadAllUser().values().stream().filter(x->x.getUpdatedAt()!= x.getCreatedAt()).toList();
    }



    private void repositoryFileCheck(){
        if(!userRepositoryFile.exists()){
            try{
                userRepositoryFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void saveAllUser(Map<UUID,User>userList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userRepositoryFile));){
            oos.writeObject(userList);
            oos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private Map<UUID,User> loadAllUser(){
        if(!userRepositoryFile.exists() || userRepositoryFile.length() == 0) {
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userRepositoryFile));){
            return ( Map<UUID,User>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

}
