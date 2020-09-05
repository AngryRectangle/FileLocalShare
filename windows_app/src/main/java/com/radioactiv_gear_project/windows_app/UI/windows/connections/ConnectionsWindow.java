package com.radioactiv_gear_project.windows_app.UI.windows.connections;

import com.radioactiv_gear_project.core.Debug;
import com.radioactiv_gear_project.windows_app.UI.UIResources;
import com.radioactiv_gear_project.windows_app.UI.AWindow;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ConnectionsWindow extends AWindow {
    public Button SettingsButton;
    public Button BackButton;
    public VBox DevicesBox;
    private Parent _parent;

    public ConnectionsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(UIResources.CONNECTIONS_WINDOW));
            _parent = loader.load();
            ObservableMap<String, Object> namespace = loader.getNamespace();

            SettingsButton = (Button) namespace.get("settingsButton");
            BackButton = (Button) namespace.get("backButton");
            DevicesBox = (VBox) namespace.get("devicesBox");
        } catch (IOException e) {
            Debug.error(e.toString());
        }
    }

    @Override
    public Parent getView() {
        return _parent;
    }
}