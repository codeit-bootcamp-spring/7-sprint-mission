package com.sprint.mission.repository.file;

import com.sprint.mission.config.DataPath;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.entity.dto.MessageDTO;
import com.sprint.mission.entity.dto.mapper.Mapper;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = DataPath.FILE_DIR + "/message.sav";
    private static final List<Message<? extends Receivable>> data = new ArrayList<>();
    private static final FileMessageRepository instance = new FileMessageRepository();

    private FileMessageRepository() {
        File file = new File(FILE_PATH);

        // 파일이 없으면 디렉토리 생성 및 빈 데이터로 시작
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }
    }

    public void init(UserRepository userRepository, ChannelRepository channelRepository) {
        if (!data.isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<Message<Receivable>> objects = ((List<MessageDTO>) ois.readObject()).stream()
                    .map(m -> Mapper.toMessage(m, userRepository, channelRepository))
                    .sorted(Comparator.comparing(Message::getCreatedAt)) // 메세지 반환시 순서 보장을 위함
                    .toList();
            data.addAll(objects);
        } catch (FileNotFoundException e) {
            // 파일이 없으면 빈 리스트로 시작
            System.out.println("메시지 파일이 없어 새로 생성합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 파일 로드 중 오류 발생", e);
        }
    }

    public static FileMessageRepository getInstance() {
        return instance;
    }

    @Override
    public void save(Message<Receivable> message) {
        data.add(message);
        write();
    }

    @Override
    public List<Message<Receivable>> findBySender(User user) {
        return data.stream()
                .filter(m -> m.getSender().equals(user))
                .map(m -> (Message<Receivable>) m)
                .toList();
    }

    @Override
    public <T extends Receivable> List<Message<T>> findByReceiver(T receiver) {
        return data.stream()
                .filter(m -> m.getReceiver().equals(receiver))
                .map(m -> (Message<T>) m)
                .toList();
    }

    @Override
    public <T extends Receivable> List<Message<T>> findBySenderAndReceiver(User user, T receiver) {
        return data.stream()
                .filter(m ->
                        m.getSender().equals(user)
                                && m.getReceiver().equals(receiver))
                .map(m -> (Message<T>) m)
                .toList();
    }

    /**
     * 테스트용 임시 메서드
     */
    private Message<Receivable> findById(UUID uuid) {
        return data.stream()
                .filter(m -> m.getUuid().equals(uuid))
                .map(m -> (Message<Receivable>) m)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id: " + uuid));
    }


    /**
     * 테스트용 임시 메서드
     */
    public <T extends Receivable> void update(Message<T> message) {
        Message<Receivable> existing = findById(message.getUuid());
        int index = data.indexOf(existing);
        if (index != -1) {
            data.set(index, (Message<? extends Receivable>) message);
            write();
        }
    }

    /**
     * 테스트용 임시 메서드: 마지막으로 저장된 메시지를 반환합니다.
     */
    public Message<Receivable> getLast() {
        if (data.isEmpty()) {
            throw new IllegalStateException("저장된 메시지가 없습니다.");
        }
        return (Message<Receivable>) data.get(data.size() - 1);
    }

    private void write() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data.stream()
                    .map(Mapper::toMessageDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
