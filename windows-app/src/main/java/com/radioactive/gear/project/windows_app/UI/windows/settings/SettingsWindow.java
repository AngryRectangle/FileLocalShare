package com.radioactive.gear.project.windows_app.UI.windows.settings;

import com.radioactive.gear.project.core.Debug;
import com.radioactive.gear.project.windows_app.UI.UIResources;
import com.radioactive.gear.project.windows_app.UI.AWindow;
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
