package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedUserDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.DeletedUser;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {
    private final ArrayList<User> userRepo ;
    private final ArrayList<DeletedUser> deletedUserRepo ;

    public FileUserRepository() {
        this.userRepo = new ArrayList<>();
        this.deletedUserRepo = new ArrayList<>();
        resetUserRepository();
    }

    @Override
    public UserDto getUserById(UUID userId)  {
        return userRepo.stream().filter(x->x.getId().equals(userId)).map(this::userToUserDto).findFirst().orElse(null);
    }

    @Override
    public UserDto getUserByName(String userName) {
        return userRepo.stream().filter(x->x.getName().equals(userName)).map(this::userToUserDto).findFirst().orElse(null);
    }

    @Override
    public UserDto getUser(UserDto userDto) {
        return getUserById(userDto.getId());
    }

    @Override
    public UserDto[] getAllUser() {
        return userRepo.stream().map(this::userToUserDto).toArray(UserDto[]::new);
    }

    @Override
    public void saveUser(UserDto userDto) {

    userRepo.add(userDtoToUser(userDto));
    }

    @Override
    public void deleteUser(UserDto userDto) {

        User targetUser = userRepo.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().orElse(null);
        if(targetUser!=null){
            deletedUserRepo.add(userDtoToDeletedUser(userDto));
            userRepo.remove(targetUser);
        }

    }

    @Override
    public <T> void updateUser(UserDto userDto, User.userElement userElement, T updatedContent) {
        User targetUser = userRepo.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().orElse(null);
        BiConsumer<User, Object> editFunction = userElement.setter;
        editFunction.accept(targetUser, updatedContent);
        targetUser.updateEntity();

    }

    @Override
    public UserDto[] getUpdatedUser() {
        return userRepo.stream().filter(x->x.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT).map(this::userToUserDto).toArray(UserDto[]::new);
    }

    @Override
    public DeletedUserDto[] getDeletedUser() {
        return deletedUserRepo.stream().map(this::deletedUserToDeletedUserDto).toArray(DeletedUserDto[]::new);
    }

    @Override
    public void addChannelToUser(UserDto userDto, ChannelDto channelDto) {
        User targetUser = userRepo.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().orElse(null);
        if(targetUser!=null){
            targetUser.addChannel(channelDtoToChannel(channelDto));
        }

    }

    @Override
    public void deleteChannelFromUser(UserDto userDto, ChannelDto channelDto) {
        User targetUser = userRepo.stream().filter(x->x.getId().equals(userDto.getId())).findFirst().orElse(null);
        if(targetUser!=null){
            targetUser.removeChannel(channelDtoToChannel(channelDto));
        }

    }

    @Override
    public void resetUserRepository() {
        userRepo.clear();
        deletedUserRepo.clear();

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
}
