package com.radioactiv_gear_project.windows_app.UI.MainMenu;

import com.radioactiv_gear_project.core.Debug;
import com.radioactiv_gear_project.windows_app.Main;
import com.radioactiv_gear_project.windows_app.UI.IView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class View implements IView {
    public Button ConnectButton;
    public Button SettingsButton;
    public Text AppName;
    private Parent parent;

    @Override
    public Parent Show() {
        try {
            parent = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
            ConnectButton = (Button) parent.lookup("#connectButton");
            SettingsButton = (Button) parent.lookup("#settingsButton");
            AppName = (Text) parent.lookup("#appName");
        } catch (IOException e) {
            Debug.error(e.toString());
        }
        return parent;
    }

    @Override
    public void Hide() {
    }
}
