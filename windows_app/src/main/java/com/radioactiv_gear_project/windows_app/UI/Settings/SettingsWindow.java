package com.radioactiv_gear_project.windows_app.UI.Settings;

import com.radioactiv_gear_project.windows_app.UI.Window;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class SettingsWindow extends Window {
    public Text SettingsText;
    private Parent parent;

    public SettingsWindow(){
        SettingsText = new Text("Settings");
        SettingsText.setLayoutX(100);
        SettingsText.setLayoutY(20);
        parent = new Group(SettingsText);
    }

    @Override
    protected Parent getView() {
        return parent;
    }
}
