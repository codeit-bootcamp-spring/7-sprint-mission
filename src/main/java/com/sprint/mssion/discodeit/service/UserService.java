package com.sprint.mssion.discodeit.service;

import com.sprint.mssion.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String username, String password, String email, String phoneNumbers, String pronoun);
    public User read(UUID uuid);
    public List<User> readAll();
    public void update(UUID uuid, String username, String password, String email, String phoneNumbers, String pronoun);
    public void delete(UUID uuid);
}
