package com.radioactive.gear.project.windows_app;

import java.net.*;
import java.util.Random;

import com.radioactive.gear.project.core.NetworkInteraction;
import com.radioactive.gear.project.core.SocketWrapper;
import com.radioactive.gear.project.windows_app.UI.*;
import com.radioactive.gear.project.windows_app.UI.windows.connections.ConnectionsController;
import com.radioactive.gear.project.windows_app.UI.windows.connections.ConnectionsWindow;
import com.radioactive.gear.project.windows_app.UI.windows.main_menu.MainMenuController;
import com.radioactive.gear.project.windows_app.UI.windows.main_menu.MainMenuWindow;
import com.radioactive.gear.project.windows_app.UI.windows.qr_code.QRCodeController;
import com.radioactive.gear.project.windows_app.UI.windows.qr_code.QRCodeWindow;
import com.radioactive.gear.project.windows_app.UI.windows.settings.SettingsController;
import com.radioactive.gear.project.windows_app.UI.windows.settings.SettingsWindow;
import com.radioactive.gear.project.windows_app.UI.views.connection.ConnectionController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public final class Main extends Application {
    public static Stage stage;
    public static AppContext appContext;

    public static void main(String[] args) {
        appContext = new AppContext();
        appContext.uniqueCode = new byte[8];
        new Random().nextBytes(appContext.uniqueCode);
        WindowService.initialize(appContext,
                new MainMenuController(new IWindowFactory<MainMenuWindow>() {
                    @Override
                    public MainMenuWindow instantiate() {
                        return new MainMenuWindow();
                    }
                }),
                new SettingsController(new IWindowFactory<SettingsWindow>() {
                    @Override
                    public SettingsWindow instantiate() {
                        return new SettingsWindow();
                    }
                }),
                new QRCodeController(new IWindowFactory<QRCodeWindow>() {
                    @Override
                    public QRCodeWindow instantiate() {
                        return new QRCodeWindow();
                    }
                }),
                new ConnectionsController(new IWindowFactory<ConnectionsWindow>() {
                    @Override
                    public ConnectionsWindow instantiate() {
                        return new ConnectionsWindow();
                    }
                })
        );

        ViewControllerService.addController(EViewType.Connection, new ConnectionController());
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
        primaryStage.setScene(new Scene(new Group()));
        WindowService.switchOn(EWindowType.MainMenu);
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