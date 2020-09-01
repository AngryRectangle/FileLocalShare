package com.radioactiv_gear_project.windows_app.UI.MainMenu;

import com.radioactiv_gear_project.windows_app.Main;
import com.radioactiv_gear_project.windows_app.UI.IController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller implements IController<View, Info> {
    @Override
    public void OnShow(final View view, final Info info) {
        /*view.ConnectButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnSettingsButton(view, info);
            }
        });
        view.SettingsButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnConnectButton(view, info);
            }
        });
        view.AppName.setText(info.appName);*/
    }

    @Override
    public void Execute(View view, Info info) {

    }

    @Override
    public void OnHide(View view, Info info) {
        view.Hide();
    }

    private void OnSettingsButton(View view, Info info) {
        OnHide(view, info);
        Controller settingsController = new Controller();
        View settingsView = new View();
        Parent group = settingsView.Show();
        settingsController.OnShow(settingsView, new Info());
        Main.stage.getScene().setRoot(group.getParent());
        Main.stage.show();
    }

    private void OnConnectButton(View view, Info info) {

    }
}
