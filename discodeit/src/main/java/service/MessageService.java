package service;

import entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message);
    Message findById(UUID id);
    List<Message> findAll();
}
