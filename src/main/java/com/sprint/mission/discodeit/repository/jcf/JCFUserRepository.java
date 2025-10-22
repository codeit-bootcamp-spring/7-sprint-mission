package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public class JCFUserRepository implements UserRepository {}
