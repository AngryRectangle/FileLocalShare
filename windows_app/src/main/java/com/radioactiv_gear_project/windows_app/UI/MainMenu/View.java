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
import java.net.URL;

public class View implements IView {
    public Button ConnectButton;
    public Button SettingsButton;
    public Text AppName;
    private Parent parent;

    @Override
    public Parent Show() {
        //ff
        try {
            URL url = getClass().getResource("/MainMenu.fxml");
            parent = FXMLLoader.load(url);
        } catch (IOException e) {
            Debug.error(e.toString());
        }
        return parent;
    }

    @Override
    public void Hide() {
    }
}
