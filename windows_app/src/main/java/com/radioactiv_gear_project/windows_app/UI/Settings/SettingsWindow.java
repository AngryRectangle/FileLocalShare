package com.radioactiv_gear_project.windows_app.UI.Settings;

import com.radioactiv_gear_project.core.Debug;
import com.radioactiv_gear_project.windows_app.UI.UIResources;
import com.radioactiv_gear_project.windows_app.UI.AWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class SettingsWindow extends AWindow {
    public Button BackButton;
    private Parent parent;

    public SettingsWindow() {
        try {
            parent = FXMLLoader.load(getClass().getResource(UIResources.SETTINGS_WINDOW));
        } catch (IOException e) {
            Debug.error(e.toString());
        }

        BackButton = (Button) parent.lookup("#backButton");
    }

    @Override
    public Parent getView() {
        return parent;
    }
}
