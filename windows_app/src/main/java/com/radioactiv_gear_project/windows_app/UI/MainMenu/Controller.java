package com.radioactiv_gear_project.windows_app.UI.MainMenu;

import com.radioactiv_gear_project.core.Debug;
import com.radioactiv_gear_project.windows_app.UI.IController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Controller implements IController<View, Info> {
    @Override
    public void OnShow(View view, Info info) {
        view.ConnectButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Debug.log("Connect", false);
            }
        });
        view.SettingsButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Debug.log("Settings", false);
            }
        });
    }

    @Override
    public void Execute(View view, Info info) {

    }

    @Override
    public void OnHide(View view, Info info) {
        view.Hide();
    }
}
