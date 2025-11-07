package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.constants.DataPath;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.dto.fileIo.BinaryContentIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.ChannelIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.MessageIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.UserIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.ReadStatusIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.UserStatusIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.mapper.Mapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserStatusRepository userStatusRepository;
    private final DataPath dataPath;

    @PostConstruct
    public void loadAll() {
        loadBinaryContent();
        loadUser();
        loadUserStatus();
        loadChannel();
        loadReadStatus();
        loadMessage();
    }

    private void loadBinaryContent() {
        File file = new File(dataPath.BINARY_CONTENT_FILE_PATH());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!binaryContentRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath.BINARY_CONTENT_FILE_PATH()))) {
            @SuppressWarnings("unchecked")
            List<BinaryContentIoDTO> list = (List<BinaryContentIoDTO>) ois.readObject();
            List<BinaryContent> objects = list.stream()
                    .map(Mapper::toBinaryContent)
                    .toList();
            binaryContentRepository.saveAll(objects);
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load binary content file", e);
        }
    }

    private void loadMessage() {
        File file = new File(dataPath.MESSAGE_FILE_PATH());

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!messageRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath.MESSAGE_FILE_PATH()))) {
            List<Message> objects = ((List<MessageIoDTO>) ois.readObject()).stream()
                    .map(m -> Mapper.toMessage(m, userRepository, channelRepository, binaryContentRepository))
                    .sorted(Comparator.comparing(Message::getCreatedAt))
                    .toList();
            for (Message message : objects) {
                messageRepository.save(message);
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load message file", e);
        }
    }

    private void loadUser() {
        File file = new File(dataPath.USER_FILE_PATH());

        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        if (!userRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath.USER_FILE_PATH()))) {
            List<User> objects = ((List<UserIoDTO>) ois.readObject()).stream()
                    .map(dto -> Mapper.toUser(dto, binaryContentRepository))
                    .toList();
            for (User user : objects) {
                userRepository.save(user);
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load user file", e);
        }
    }

    private void loadChannel() {
        File file = new File(dataPath.CHANNEL_FILE_PATH());

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!channelRepository.findAll().isEmpty()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath.CHANNEL_FILE_PATH()))) {
            List<Channel> objects = ((List<ChannelIoDTO>) ois.readObject()).stream()
                    .map(c -> Mapper.toChannel(c, userRepository))
                    .toList();
            for (Channel object : objects) {
                channelRepository.save(object);
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load channel file", e);
        }
    }

    private void loadReadStatus() {
        File file = new File(dataPath.READ_STATUS_FILE_PATH());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!readStatusRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath.READ_STATUS_FILE_PATH()))) {
            @SuppressWarnings("unchecked")
            List<ReadStatusIoDTO> list = (List<ReadStatusIoDTO>) ois.readObject();
            List<ReadStatus> objects = list.stream()
                    .map(dto -> Mapper.toReadStatus(dto, userRepository, channelRepository))
                    .toList();
            for (ReadStatus rs : objects) {
                readStatusRepository.save(rs);
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load read status file", e);
        }
    }

    private void loadUserStatus() {
        File file = new File(dataPath.USER_STATUS_FILE_PATH());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!userStatusRepository.findAll().isEmpty()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath.USER_STATUS_FILE_PATH()))) {
            @SuppressWarnings("unchecked")
            List<UserStatusIoDTO> list = (List<UserStatusIoDTO>) ois.readObject();
            List<UserStatus> objects = list.stream()
                    .map(dto -> Mapper.toUserStatus(dto, userRepository))
                    .toList();
            for (UserStatus us : objects) {
                userStatusRepository.save(us);
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load user status file", e);
        }
    }
}
