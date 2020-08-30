package com.radioactiv_gear_project.windows_app;

import java.awt.*;
import java.net.*;
import com.radioactiv_gear_project.core.Debug;
import com.radioactiv_gear_project.core.NetworkInteraction;
import com.radioactiv_gear_project.core.SocketWrapper;
import com.radioactiv_gear_project.windows_app.UI.MainMenu.Controller;
import com.radioactiv_gear_project.windows_app.UI.MainMenu.Info;
import com.radioactiv_gear_project.windows_app.UI.MainMenu.View;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public final class Main extends Application {
    public static void main(String[] args) {
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
    public void start(Stage primaryStage) throws Exception {
        Controller mainMenuController = new Controller();
        View mainMenuView = new View();
        Text text = new Text("Hello from JavaFX!");
        text.setLayoutY(80);
        text.setLayoutX(100);

        Group group = mainMenuView.Show();
        mainMenuController.OnShow(mainMenuView, new Info());

        Scene scene = new Scene(group);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FileLocalShare");
        primaryStage.setWidth(800);
        primaryStage.setHeight(1000);
        primaryStage.show();
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