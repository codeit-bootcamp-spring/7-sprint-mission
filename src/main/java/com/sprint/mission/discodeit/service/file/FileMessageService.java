package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileMessageService implements MessageService {
    private final List<Message> messageStore = new ArrayList<>();
    private final String MESSAGE_FILE;

    public FileMessageService(String MESSAGE_FILE) {
        this.MESSAGE_FILE = MESSAGE_FILE;
        loadMessagesFromFile(); // 서비스 시작 시 파일 로드
    }

    // --- 파일 저장 (직렬화) ---
    private void saveMessagesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MESSAGE_FILE))) {
            oos.writeObject(messageStore);
            System.out.println("[저장 완료] 메시지 데이터 파일 저장됨");
        } catch (IOException e) {
            System.err.println("[오류] 메시지 저장 실패: " + e.getMessage());
        }
    }

    // --- 파일 불러오기 (역직렬화) ---
    private void loadMessagesFromFile() {
        File file = new File(MESSAGE_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_FILE))) {
            List<Message> loaded = (List<Message>) ois.readObject();
            messageStore.clear();
            messageStore.addAll(loaded);
            System.out.println("[로드 완료] 메시지 데이터 로드됨");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[오류] 메시지 로드 실패: " + e.getMessage());
        }
    }


    @Override
    public <T> void createMessage(User user, T receiver, String content) {
        Message newMessage;

        if(receiver instanceof User user2){
            newMessage = new Message(user.getId(), user2.getId(), Message.ReceiveType.USER, content);
        } else if(receiver instanceof Channel channel){
            newMessage = new Message(user.getId(), channel.getId(), Message.ReceiveType.CHANNEL, content);
        } else return;

        messageStore.add(newMessage);
        saveMessagesToFile();
    }

    @Override
    public <T> Message getLastestMessage(User user, T receiver) {
        // 최신순(역순)으로 순회
        Stream<Message> reversed = IntStream.iterate(messageStore.size() - 1, i -> i - 1)
                .limit(messageStore.size())
                .mapToObj(index -> messageStore.get(index));

        if(receiver instanceof User user2){
            return reversed.filter(m ->
                            (m.getSenderId().equals(user.getId()) && m.getReceiverId().equals(user2.getId())) ||
                                    (m.getSenderId().equals(user2.getId()) && m.getReceiverId().equals(user.getId())))
                    .findFirst().orElse(null);
        } else if(receiver instanceof Channel channel){
            return reversed.filter(m ->
                            m.getSenderId().equals(user.getId()) && m.getReceiverId().equals(channel.getId()))
                    .findFirst().orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public List<Message> getMessagesBetween(User user1, User user2) {
        return messageStore.stream()
                .filter(m -> (m.getSenderId().equals(user1.getId()) && m.getReceiverId().equals(user2.getId())) ||
                        (m.getSenderId().equals(user2.getId()) && m.getReceiverId().equals(user1.getId())))
                .toList();
    }

    @Override
    public List<Message> getAllMessagesByUser(User user) {
        return messageStore.stream()
                .filter(m -> m.getSenderId().equals(user.getId()) || m.getReceiverId().equals(user.getId()))
                .toList();
    }

    @Override
    public List<Message> getAllByChannel(Channel channel) {
        return messageStore.stream()
                .filter(m -> m.getReceiverId().equals(channel.getId()))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        for(int i = 0; i < messageStore.size(); i++){
            if(messageStore.get(i).getId().equals(id)){
                Message message = messageStore.get(i);
                message.setContents(content);
                messageStore.set(i, message);
                saveMessagesToFile();
                break;
            }
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        messageStore.removeIf(m -> m.getId().equals(id));
        saveMessagesToFile();
    }

    @Override
    public void deleteMessagesByUser(User user) {
        messageStore.removeIf(m -> m.getSenderId().equals(user.getId()));
        saveMessagesToFile();
    }
}
