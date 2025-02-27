package com.radioactive.gear.project.windows_app.UI.windows.main_menu;

import com.radioactive.gear.project.core.Debug;
import com.radioactive.gear.project.windows_app.UI.UIResources;
import com.radioactive.gear.project.windows_app.UI.AWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainMenuWindow extends AWindow {
    public Button ConnectButton;
    public Button SettingsButton;
    public Text AppName;
    private Parent parent;

    public MainMenuWindow() {
        try {
            parent = FXMLLoader.load(getClass().getResource(UIResources.MAIN_MENU_WINDOW));
        } catch (IOException e) {
            Debug.error(e.toString());
        }

        ConnectButton = (Button) parent.lookup("#connectButton");
        SettingsButton = (Button) parent.lookup("#settingsButton");
        AppName = (Text) parent.lookup("#appName");
    }

    @Override
    public Parent getView() {
        return parent;
    }
}
