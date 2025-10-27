package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.Receivable;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.dto.UserDTO;
import com.sprint.mission.discodeit.entity.dto.mapper.Mapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Comparator;
import java.util.List;

import static com.sprint.mission.discodeit.config.DataPath.*;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public void loadAll() {
        loadUser();
        loadChannel();
        loadMessage();
    }

    private void loadMessage() {
        File file = new File(MESSAGE_FILE_PATH);

        // 파일이 없으면 디렉토리 생성 및 빈 데이터로 시작
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!messageRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_FILE_PATH))) {
            List<Message<Receivable>> objects = ((List<MessageDTO>) ois.readObject()).stream()
                    .map(m -> Mapper.toMessage(m, userRepository, channelRepository))
                    .sorted(Comparator.comparing(Message::getCreatedAt)) // 메세지 반환시 순서 보장을 위함
                    .toList();
            for (Message<Receivable> message : objects) {
                messageRepository.save(message);
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 빈 리스트로 시작
            System.out.println("메시지 파일이 없어 새로 생성합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 파일 로드 중 오류 발생", e);
        }
    }

    private void loadUser() {
        File file = new File(USER_FILE_PATH);

        // 파일이 없으면 디렉토리 생성 및 빈 데이터로 시작
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        if (!userRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE_PATH))) {
            List<User> objects = ((List<UserDTO>) ois.readObject()).stream()
                    .map(Mapper::toUser)
                    .toList();
            for (User user : objects) {
                userRepository.save(user);
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 빈 맵으로 시작
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 파일 로드 중 오류 발생", e);
        }
    }

    private void loadChannel() {
        File file = new File(CHANNEL_FILE_PATH);

        // 파일이 없으면 디렉토리 생성 및 빈 데이터로 시작
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!channelRepository.findAll().isEmpty()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHANNEL_FILE_PATH))) {
            List<Channel> objects = ((List<ChannelDTO>) ois.readObject()).stream()
                    .map(c -> Mapper.toChannel(c, userRepository))
                    .toList();
            for (Channel object : objects) {
                channelRepository.save(object);
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 빈 맵으로 시작
            System.out.println("채널 파일이 없어 새로 생성합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 파일 로드 중 오류 발생", e);
        }
    }
}
