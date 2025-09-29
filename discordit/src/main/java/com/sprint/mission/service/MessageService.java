package com.sprint.mission.service;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;

import java.util.Map;

public interface MessageService<T extends Receivable> {
    public void sendMessage(User sender, T receiver, String message);
}
