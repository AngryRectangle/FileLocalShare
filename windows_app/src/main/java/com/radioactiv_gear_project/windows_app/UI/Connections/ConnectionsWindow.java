package com.radioactiv_gear_project.windows_app.UI.Connections;

import com.radioactiv_gear_project.core.Debug;
import com.radioactiv_gear_project.windows_app.UI.UIResources;
import com.radioactiv_gear_project.windows_app.UI.AWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.io.IOException;

public class ConnectionsWindow extends AWindow {
    public Button SettingsButton;
    public Button BackButton;
    public ScrollPane DevicesPane;
    private Parent parent;

    public ConnectionsWindow() {
        try {
            parent = FXMLLoader.load(getClass().getResource(UIResources.CONNECTIONS_WINDOW));
        } catch (IOException e) {
            Debug.error(e.toString());
        }

        SettingsButton = (Button) parent.lookup("#settingsButton");
        BackButton = (Button) parent.lookup("#backButton");
        DevicesPane = (ScrollPane) parent.lookup("#devicesPane");
    }

    @Override
    protected Parent getView() {
        return parent;
    }
}
