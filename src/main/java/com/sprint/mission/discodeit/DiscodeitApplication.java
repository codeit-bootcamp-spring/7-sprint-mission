package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.auth.AuthUser;
import com.sprint.mission.discodeit.utils.AppConfig;
import com.sprint.mission.discodeit.utils.ConfigurationLoader;
import com.sprint.mission.discodeit.view.LoginViewController;
import com.sprint.mission.discodeit.view.MainViewController;
import com.sprint.mission.discodeit.view.RegisterViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DiscodeitApplication extends Application {

    private static AppConfig appConfig;
    private static ConfigurationLoader configLoader;

    public static void setAppConfig(AppConfig config) {
        appConfig = config;
        configLoader = appConfig.getConfigLoader();
    }

    public static AppConfig getAppConfig() {
        return appConfig;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        setupLoginStage(primaryStage, "Discodeit Client 1", 100);
    }

    private void setupLoginStage(Stage stage, String title, double xPosition) throws IOException {
        FXMLLoader loader = createLoaderFromKey("view.login");
        Parent root = loader.load();
        LoginViewController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle(title);
        stage.setX(xPosition);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void showLoginView(Stage stage) throws IOException {
        FXMLLoader loader = createLoaderFromKey("view.login");
        Parent root = loader.load();

        LoginViewController controller = loader.getController();
        controller.setStage(stage);

        stage.setTitle(stage.getTitle().split(" - ")[0] + " - Login");
        stage.setScene(new Scene(root, 600, 400));
    }

    public static void showRegisterView(Stage stage) throws IOException {
        FXMLLoader loader = createLoaderFromKey("view.register");
        Parent root = loader.load();
        RegisterViewController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle(stage.getTitle().split(" - ")[0] + " - Register");
        stage.setScene(new Scene(root, 600, 500));
    }

    public static void showMainView(Stage stage, AuthUser authUser) throws IOException {
        FXMLLoader loader = createLoaderFromKey("view.main");
        Parent root = loader.load();
        MainViewController controller = loader.getController();
        controller.initData(authUser, stage); // Stage도 함께 전달
        stage.setTitle("Discodeit - " + authUser.nickname());
        stage.setScene(new Scene(root, 1280, 720));
    }

    private static FXMLLoader createLoaderFromKey(String fxmlKey) {
        String fxmlPath = configLoader.getProperty(fxmlKey);
        URL location = DiscodeitApplication.class.getResource(fxmlPath);
        return new FXMLLoader(location);
    }

    @Override
    public void stop() {
        if (appConfig != null) {
            appConfig.getUserService().goOffline(appConfig.getAuthService().getCurrentUser().id());
            System.out.println("애플리케이션을 종료하며 데이터를 저장합니다...");
            appConfig.saveAllData();
        }
    }
}