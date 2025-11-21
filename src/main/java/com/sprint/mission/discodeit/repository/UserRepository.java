package com.sprint.mission.discodeit.repository;


import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

//    public Optional<User> getUserById(UUID userId) ;
//
//
//
//    public Optional<User> getUserByName(String userName);
//    public Optional<User> getUser(User user);
//    public List<User> getAllUser();
//    public User saveUser(User userDto);
//    public void deleteUser(UUID userId);
//    public void updateUser(User user);
//    public List<User> getUpdatedUser();
////    public DeletedUserDto[] getDeletedUser();
////    public void addChannelToUser(User user, Channel channel);
////    public void deleteChannelFromUser(User userDto, Channel channel);
//    public void resetUserRepository();
//    public boolean isUserExit(UUID userId);


}
