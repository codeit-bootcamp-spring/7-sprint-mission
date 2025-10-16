package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedUserDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.DeletedUser;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.util.AppendableObjectOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class FileUserService implements UserRepository {
    @Override
    public void addChannelToUser(UserDto userDto, ChannelDto channelDto) {
        List<User> userDb = loadAllUser();
        User targetUser = userDb.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().get();
        targetUser.addChannel(channelDtoToChannel(channelDto));
        saveAllUser(userDb);
    }

    @Override
    public void deleteChannelFromUser(UserDto userDto, ChannelDto channelDto) {
        if(userDto==null || channelDto==null){
            return;
        }
       List<User> userDb = loadAllUser();
        User targetUser = userDb.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().orElse(null);
        targetUser.removeChannel(channelDtoToChannel(channelDto));
        saveAllUser(userDb);
    }

    @Override
    public void resetUserRepository() {
        saveAllUser(new ArrayList<>());
        saveAllDeletedUser(new ArrayList<>());

    }

    private final String DATA_ROOT = "C:\\Users\\황준영\\Java-codeit\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\data\\";
    private final String USER_DATA_ROOT = DATA_ROOT + "userRepository.ser";
    private final String DELETED_USER_DATA_ROOT = DATA_ROOT + "deletedUserRepository.ser";
    private File userRepositoryFile = new File(USER_DATA_ROOT);
    private File deletedUserRepositoryFile = new File(DELETED_USER_DATA_ROOT);

    public FileUserService() {
        repositoryFileCheck();
        resetUserRepository();
    }

    @Override
    public UserDto getUserById(UUID userId) {
       return loadAllUser().stream().filter(x->x.getId().equals(userId)).map(this::userToUserDto).findFirst().orElse(null);

    }

    @Override
    public UserDto getUser(UserDto userDto) {

       return getUserById(userDto.getId());
    }

    @Override
    public UserDto getUserByName(String userName) {
       return loadAllUser().stream().filter(x->x.getName().equals(userName)).map(this::userToUserDto).findFirst().orElse(null);
    }

    @Override
    public UserDto[] getAllUser() {
     return loadAllUser().stream().map(this::userToUserDto).toArray(UserDto[]::new);
    }

    @Override
    public void saveUser(UserDto userDto) {
      List<User> userDb = loadAllUser();
       userDb.add(userDtoToUser(userDto));
       saveAllUser(userDb);
       return;

    }

    @Override
    public void deleteUser(UserDto userDto) {

        List<User> userDb = loadAllUser();
        userDb.remove(userDtoToUser(userDto));

        List<DeletedUser> deletedUserDb = loadAllDeletedUser();
        deletedUserDb.add(userDtoToDeletedUser(userDto));
        saveAllDeletedUser(deletedUserDb);
        saveAllUser(userDb);
        return;
    }

    @Override
    public <T> void updateUser(UserDto userDto, User.userElement userElement, T updatedContent) {
       List<User> users = loadAllUser();
       User targetUser = users.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().orElse(null);
       BiConsumer<User, Object> editFunction = userElement.setter;
       editFunction.accept(targetUser, updatedContent);
       targetUser.updateEntity();
       saveAllUser(users);

    }

    @Override
    public UserDto[] getUpdatedUser() {
       return loadAllUser().stream().filter(x->x.getUpdatedAt()!=Entity.DEFAULT_UPDATED_AT).map(this::userToUserDto).toArray(UserDto[]::new);
    }

    @Override
    public DeletedUserDto[] getDeletedUser() {
        return loadAllDeletedUser().stream().map(this::deletedUserToDeletedUserDto).toArray(DeletedUserDto[]::new);
    }

    private void repositoryFileCheck(){
        if(!userRepositoryFile.exists()){
            try{
                userRepositoryFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!deletedUserRepositoryFile.exists()){
            try{
                deletedUserRepositoryFile.createNewFile();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private Channel channelDtoToChannel(ChannelDto channelDto){
        List<User> userDb = channelDto.getUserDtoList().stream().map(
                x->new User(x.getId(),x.getName(),x.getNickname(),x.getEmail(),x.isOnline())
        ).collect(Collectors.toList());
        return new Channel(channelDto.getId(),channelDto.getName(),channelDto.getDescription(),channelDto.isPublic(),channelDto.isTextChannel(),userDb);
    }
    private UserDto userToUserDto(User user){
        List<ChannelDto> channelDtoList = user.getChannelDb().stream()
                .map(x->new ChannelDto(x.getId(),x.getName(),x.getDescription(),x.isPublic(),x.isTextChannel())).collect(Collectors.toList());
        return new UserDto(user.getId(),user.getName(),user.getNickname(),user.getEmail(),user.isOnline(),channelDtoList);
    }
    private User userDtoToUser(UserDto userDto){
        return new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
    }

    private DeletedUserDto deletedUserToDeletedUserDto(DeletedUser deletedUser){
        return new DeletedUserDto(deletedUser.getName());
    }
    private DeletedUser deletedUserDtoToDeletedUser(DeletedUserDto deletedUserDto){
        return new DeletedUser(deletedUserDto.getName());
    }

    private DeletedUser userDtoToDeletedUser(UserDto userDto){
        return new DeletedUser(userDto.getName());
    }

    private void saveAllUser(List<User>userList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_ROOT));){
            oos.writeObject(userList);
            oos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private List<User> loadAllUser(){
        if(!userRepositoryFile.exists() || userRepositoryFile.length() == 0) {
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_ROOT));){
            return (List<User>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<DeletedUser> loadAllDeletedUser(){
        if(!deletedUserRepositoryFile.exists() || deletedUserRepositoryFile.length() == 0) {
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELETED_USER_DATA_ROOT));){
            return (List<DeletedUser>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void saveAllDeletedUser(List<DeletedUser>deletedUserList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DELETED_USER_DATA_ROOT));){
            oos.writeObject(deletedUserList);
            oos.flush();
        }
        catch (Exception e){}
    }

}
