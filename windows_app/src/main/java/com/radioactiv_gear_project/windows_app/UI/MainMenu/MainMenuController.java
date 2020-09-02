package com.radioactiv_gear_project.windows_app.UI.MainMenu;

import com.radioactiv_gear_project.windows_app.UI.IController;
import com.radioactiv_gear_project.windows_app.UI.Settings.SettingsController;
import com.radioactiv_gear_project.windows_app.UI.Settings.SettingsWindow;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MainMenuController implements IController<MainMenuWindow, Info> {
    @Override
    public void OnShow(final MainMenuWindow view, final Info info) {
        view.ConnectButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnConnectButton(view, info);
            }
        });
        view.SettingsButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnSettingsButton(view, info);
            }
        });
        view.AppName.setText(info.appName);
    }

    @Override
    public void Execute(MainMenuWindow view, Info info) {

    }

    @Override
    public void OnHide(MainMenuWindow view, Info info) {

    }

    private void OnSettingsButton(MainMenuWindow view, Info info) {
        OnHide(view, info);
        SettingsController settingsController = new SettingsController();
        SettingsWindow settingsWindow = new SettingsWindow();
        settingsWindow.show();
        settingsController.OnShow(settingsWindow, new com.radioactiv_gear_project.windows_app.UI.Settings.Info());
    }

    private void OnConnectButton(MainMenuWindow view, Info info) {

    }
}
