package com.sprint.mission.service;

import com.sprint.mission.entity.User;

public interface MessageService {
    public void display(User sender, User receiver, String message);
}
