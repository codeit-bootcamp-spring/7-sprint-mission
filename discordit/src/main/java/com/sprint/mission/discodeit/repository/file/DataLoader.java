package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.dto.fileIo.ChannelIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.MessageIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.UserIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.mapper.Mapper;
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

        // 파일이 없으면 디렉토리 생성 후 종료
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!messageRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_FILE_PATH))) {
            List<Message> objects = ((List<MessageIoDTO>) ois.readObject()).stream()
                    .map(m -> Mapper.toMessage(m, userRepository, channelRepository))
                    .sorted(Comparator.comparing(Message::getCreatedAt)) // 메시지 반환 순서 보장을 위해 정렬
                    .toList();
            for (Message message : objects) {
                messageRepository.save(message);
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 무시하고 동작
            System.out.println("메시지 파일이 없어 새로 생성합니다");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 파일 로드 중 오류 발생", e);
        }
    }

    private void loadUser() {
        File file = new File(USER_FILE_PATH);

        // 파일이 없으면 디렉토리 생성
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        if (!userRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE_PATH))) {
            List<User> objects = ((List<UserIoDTO>) ois.readObject()).stream()
                    .map(Mapper::toUser) // TODO: BinaryContentRepository 연계하여 프로필 이미지 복원 (차후 예정)
                    .toList();
            for (User user : objects) {
                userRepository.save(user);
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 무시
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 파일 로드 중 오류 발생", e);
        }
    }

    private void loadChannel() {
        File file = new File(CHANNEL_FILE_PATH);

        // 파일이 없으면 디렉토리 생성 후 종료
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!channelRepository.findAll().isEmpty()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHANNEL_FILE_PATH))) {
            List<Channel> objects = ((List<ChannelIoDTO>) ois.readObject()).stream()
                    .map(c -> Mapper.toChannel(c, userRepository))
                    .toList();
            for (Channel object : objects) {
                channelRepository.save(object);
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 무시하고 동작
            System.out.println("채널 파일이 없어 새로 생성합니다");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 파일 로드 중 오류 발생", e);
        }
    }
}

