package com.radioactiv_gear_project.windows_app.UI.Settings;

import com.radioactiv_gear_project.windows_app.UI.IView;
import javafx.scene.Group;
import javafx.scene.text.Text;

public class View implements IView {
    public Text SettingsText;
    @Override
    public Group Show() {
        SettingsText = new Text("Settings");
        SettingsText.setLayoutX(100);
        SettingsText.setLayoutY(20);
        Group parent = new Group(SettingsText);
        return parent;
    }

    @Override
    public void Hide() {

    }
}
