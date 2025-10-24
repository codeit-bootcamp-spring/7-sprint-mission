package com.sprint.mission.discodeit.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * config.properties 파일의 내용을 읽어와 Properties 객체로 제공하는 클래스입니다.
 */
public class ConfigurationLoader {

    private final Properties properties = new Properties();

    public ConfigurationLoader(String configFileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                System.err.println("설정 파일을 찾을 수 없습니다: " + configFileName);
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("설정 파일 로딩 중 오류 발생");
            ex.printStackTrace();
        }
    }

    /**
     * 주어진 키(key)에 해당하는 설정 값(value)을 반환합니다.
     * @param key 설정 키
     * @return 설정 값
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 키가 없을 경우를 대비하여 기본값을 사용할 수 있는 메서드입니다.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}