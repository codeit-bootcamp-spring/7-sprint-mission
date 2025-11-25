package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.repository.jpainterface.JpaUserRepository;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpa;
    private final UserMapper mapper;


    @Override
    public User save(User user) {

        UserEntity userEntity = mapper.toUserEntity(user);

//        UserEntity userEntity = new UserEntity(user.getUsername(),user.getEmail(), user.getPassword());
        UserEntity save = jpa.save(userEntity);
        return mapper.toUser(save);
    }

    @Override
    public void updateProfileId(String id, String profileId) {
        UserEntity userEntity = jpa.findById(UUID.fromString(id)).orElseThrow(()->new NoSuchElementException("dhfb"));
        userEntity.setProfileId(UUID.fromString(profileId));
    }

    @Override
    public void delete(User user) {
        UserEntity userEntity = mapper.toUserEntity(user);
        jpa.delete(userEntity);
    }

    @Override
    public Optional<User> findById(String id) {

        return jpa.findById(UUID.fromString(id)).map(mapper::toUser);
    }

    @Override
    public List<User> findAll() {
        return jpa.findAll().stream().map(mapper::toUser).toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(mapper::toUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByUsername(username).map(mapper::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUsername(username);
    }
}
