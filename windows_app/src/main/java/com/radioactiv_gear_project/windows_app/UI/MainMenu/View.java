package com.radioactiv_gear_project.windows_app.UI.MainMenu;

import com.radioactiv_gear_project.windows_app.UI.IView;
import javafx.scene.Group;
import javafx.scene.control.Button;

public class View implements IView {
    public Button ConnectButton;
    public Button SettingsButton;
    private Group parent;

    @Override
    public Group Show() {
        ConnectButton = new Button("Connect");
        ConnectButton.setLayoutX(100);
        ConnectButton.setLayoutY(100);
        SettingsButton = new Button("Settings");
        SettingsButton.setLayoutX(100);
        SettingsButton.setLayoutY(200);
        parent = new Group(ConnectButton, SettingsButton);
        return parent;
    }

    @Override
    public void Hide() {
        parent.getChildren().removeAll();
    }
}
