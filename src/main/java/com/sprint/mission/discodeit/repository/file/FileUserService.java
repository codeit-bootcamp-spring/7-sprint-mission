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
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class FileUserService implements UserRepository {
    @Override
    public void addChannelToUser(UserDto userDto, ChannelDto channelDto) {
        if(userDto==null || channelDto==null){
            return;
        }
        try(
                ObjectInputStream ois = new ObjectInputStream
                        (new FileInputStream
                                (USER_DATA_ROOT));
                ObjectOutputStream oos = new ObjectOutputStream
                        (new FileOutputStream
                                (USER_DATA_ROOT));
        ){

            List<User> userDb = (List<User>) ois.readObject();
            User tempUser = userDb.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().get();


            tempUser.addChannel(channelDtoToChannel(channelDto));

            userDb.remove(userDtoToUser(userDto));
            userDb.add(tempUser);
            oos.writeObject(userDb);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteChannelFromUser(UserDto userDto, ChannelDto channelDto) {
        if(userDto==null || channelDto==null){
            return;
        }
        try(
                ObjectInputStream ois = new ObjectInputStream
                        (new FileInputStream
                                (USER_DATA_ROOT));
                ObjectOutputStream oos = new ObjectOutputStream
                        (new FileOutputStream
                                (USER_DATA_ROOT));
        )
        {

            List<User> userDb = (List<User>) ois.readObject();
            User tempUser = userDb.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().get();
            tempUser.removeChannel(channelDtoToChannel(channelDto));
            userDb.remove(userDtoToUser(userDto));
            userDb.add(tempUser);
            oos.writeObject(userDb);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    private final String DATA_ROOT = "C:\\Users\\황준영\\Java-codeit\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\data\\";
    private final String USER_DATA_ROOT = DATA_ROOT + "userRepository.cer";
    private final String DELETED_USER_DATA_ROOT = DATA_ROOT + "deletedUserRepository.cer";
    private File userRepositoryFile = new File(USER_DATA_ROOT);
    private File deletedUserRepositoryFile = new File(DELETED_USER_DATA_ROOT);

    public FileUserService() {
        repositoryFileCheck();
    }

    @Override
    public UserDto getUserById(UUID userId) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_ROOT));
        List<User> userDb = (List<User>) ois.readObject();
        boolean isExist = userDb.stream().anyMatch(x -> x.getId().equals(userId));
        if(!isExist) return null;
        User result = userDb.stream().filter(x->x.getId().equals(userId)).findFirst().get();
        ois.close();
        return userToUserDto(result);

    }

    @Override
    public UserDto getUser(UserDto userDto) {

        try
        {
            return getUserById(userDto.getId());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDto getUserByName(String userName) {
        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_ROOT));
                )
        {
            List<User> userDb = (List<User>) ois.readObject();
            boolean isExist = userDb.stream().anyMatch(x -> x.getName().equals(userName));
            if(!isExist) return null;
            User result = userDb.stream().filter(x->x.getName().equals(userName)).findFirst().get();
            return userToUserDto(result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDto[] getAllUser() {
        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_ROOT));
                )
        {
            List<User> userDb = (List<User>) ois.readObject();
            return userDb.stream().map(this::userToUserDto).toArray(UserDto[]::new);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveUser(UserDto userDto) {
        try(
                ObjectOutputStream oos = new AppendableObjectOutputStream(new FileOutputStream(USER_DATA_ROOT,true))
                )
        {
         oos.writeObject(userDtoToUser(userDto));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void deleteUser(UserDto userDto) {
        try(
                ObjectOutputStream oosDeleted = new AppendableObjectOutputStream(new FileOutputStream(DELETED_USER_DATA_ROOT,true));
                ObjectOutputStream oosUser = new ObjectOutputStream(new FileOutputStream(USER_DATA_ROOT));
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_ROOT));
                )
        {
         List<User> tempUserLis = (List<User>) ois.readObject();
         tempUserLis.remove(userDtoToUser(userDto));
         oosUser.writeObject(tempUserLis);
         oosDeleted.writeObject(userDtoToDeletedUser(userDto));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public <T> void updateUser(UserDto userDto, User.userElement userElement, T updatedContent) {
        try(
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_ROOT));
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_ROOT));
                ){
            User updatedUser = userDtoToUser(userDto);
            BiConsumer<User, Object> editFunction = userElement.setter;
            editFunction.accept(updatedUser, updatedContent);
            updatedUser.updateEntity();
            List<User> userDb = (List<User>) ois.readObject();
            userDb.remove(userDtoToUser(userDto));
            userDb.add(updatedUser);
            oos.writeObject(userDb);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public UserDto[] getUpdatedUser() {
        try
            (
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_ROOT));
                )
        {
            List<User> userDb = (List<User>) ois.readObject();
            return userDb.stream().filter(x->x.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT).map(this::userToUserDto).toArray(UserDto[]::new);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDto[] getDeletedUser() {
        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELETED_USER_DATA_ROOT));
                ){
            List<DeletedUser> deletedUserDb = (List<DeletedUser>) ois.readObject();
            return deletedUserDb.stream().map(this::deletedUserToDeletedUserDto).toArray(UserDto[]::new);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
        ).toList();
        return new Channel(channelDto.getId(),channelDto.getName(),channelDto.getDescription(),channelDto.isPublic(),channelDto.isTextChannel(),userDb);
    }
    private UserDto userToUserDto(User user){
        List<ChannelDto> channelDtoList = user.getChannelDb().stream()
                .map(x->new ChannelDto(x.getId(),x.getName(),x.getDescription(),x.isPublic(),x.isTextChannel())).toList();
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
}
