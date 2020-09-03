package com.radioactiv_gear_project.windows_app;

import java.net.*;

import com.radioactiv_gear_project.core.NetworkInteraction;
import com.radioactiv_gear_project.core.SocketWrapper;
import com.radioactiv_gear_project.windows_app.UI.EWindowType;
import com.radioactiv_gear_project.windows_app.UI.IWindowFactory;
import com.radioactiv_gear_project.windows_app.UI.MainMenu.MainMenuController;
import com.radioactiv_gear_project.windows_app.UI.MainMenu.MainMenuWindow;
import com.radioactiv_gear_project.windows_app.UI.Settings.SettingsController;
import com.radioactiv_gear_project.windows_app.UI.Settings.SettingsWindow;
import com.radioactiv_gear_project.windows_app.UI.WindowService;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public final class Main extends Application {
    public static Stage stage;
    public static AppContext appContext;
    public static WindowService windowService;

    public static void main(String[] args) {
        windowService = new WindowService(new MainMenuController(new IWindowFactory<MainMenuWindow>() {
            @Override
            public MainMenuWindow instantiate() {
                return new MainMenuWindow();
            }
        }), new SettingsController(new IWindowFactory<SettingsWindow>() {
            @Override
            public SettingsWindow instantiate() {
                return new SettingsWindow();
            }
        }));

        Application.launch(Main.class, args);

        try {
            String pcName = InetAddress.getLocalHost().getHostName();
            DatagramPacket packet = NetworkInteraction.receivePacket(NetworkInteraction.DEFAULT_PC_GROUP);
            sleep(10);
            NetworkInteraction.multicast(pcName.getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(5);
            NetworkInteraction.multicast(("SecondPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(120);
            NetworkInteraction.multicast(("ThirdPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
            sleep(1200);
            NetworkInteraction.multicast(("FourthPC").getBytes(), NetworkInteraction.DEFAULT_ANDROID_GROUP);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            Socket socket = NetworkInteraction.host();
            System.out.println("Connected");
            SocketWrapper wrapper = new SocketWrapper(socket);
            wrapper.startProgressSending(SocketWrapper.PROGRESS_SENDING_DELAY);
            wrapper.addListener(new FileReceiver(wrapper));
            while (true) {
                wrapper.executeListeners();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        appContext = new AppContext();
        primaryStage.setScene(new Scene(new Group()));
        windowService.Open(EWindowType.MainMenu);
        primaryStage.setTitle("FileLocalShare");
    }

    public static class FileReceiver implements SocketWrapper.CodeReceiveHandler {
        SocketWrapper wrapper;

        public FileReceiver(SocketWrapper w) {
            wrapper = w;
        }

        public void execute(SocketWrapper.InteractionType type) {
            try {
                if (type == SocketWrapper.InteractionType.DATA_SENDING) {
                    System.out.println("Start receiving");
                    wrapper.receiveData("A:/FileLocalShare/");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}