package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.MessageService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JCFMessageService implements MessageService {
    public static final JCFMessageService instance = new JCFMessageService();
    private static final List<Message<? extends Receivable>> data = new ArrayList<>();

    private JCFMessageService() {
    }

    public static JCFMessageService getInstance() {
        return instance;
    }

    public void sendMessage(User sender, Receivable receiver, String message){
        Message<Receivable> m = new Message<>(sender, receiver, message);
        data.add(m);
        display(m);
    }

    public List<Message<Receivable>> getBySender(User sender){
        return data.stream()
                .filter(m -> m.getSender().equals(sender))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(m -> (Message<Receivable>) m)
                .toList();
    }

    public <T extends Receivable> List<Message<T>> getByReceiver(T receiver) {
        return data.stream()
                .filter(m -> m.getReceiver().equals(receiver))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(m -> (Message<T>) m)
                .toList();
    }

    public <T extends  Receivable> List<Message<T>> getBySenderAndReceiver(User sender, T receiver) {
        return data.stream()
                .filter(m ->  m.getSender().equals(sender)
                            && m.getReceiver().equals(receiver))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(m -> (Message<T>) m)
                .toList();
    }

    private boolean isSameDate(long unixTimeStamp, LocalDateTime date) {
        LocalDateTime timestampDate = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochSecond(unixTimeStamp),
                java.time.ZoneId.systemDefault()
        );
        return timestampDate.toLocalDate().equals(date.toLocalDate());
    }

    private<T extends Receivable> void display(Message<T> message) {
        System.out.printf("[%s] -> [%s] \n%s (%s)\n",
                message.getSender().getUserId(),
                message.getReceiver().getDisplayName(),
                message.getMessage(),
                message.getCreatedTime());
    }

    /**
     * 테스트용 메서드: 마지막으로 추가된 메시지를 반환합니다.
     * @return 마지막 메시지
     */
    public Message<? extends Receivable> getLastMessage() {
        if (data.isEmpty()) {
            return null;
        }
        return data.get(data.size() - 1);
    }
}
