package com.radioactiv_gear_project.windows_app.UI.Connections;

import com.radioactiv_gear_project.windows_app.Main;
import com.radioactiv_gear_project.windows_app.UI.AWindowController;
import com.radioactiv_gear_project.windows_app.UI.EWindowType;
import com.radioactiv_gear_project.windows_app.UI.IWindowFactory;
import com.radioactiv_gear_project.windows_app.UI.WindowService;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ConnectionsController extends AWindowController<ConnectionsWindow> {
    public ConnectionsController(IWindowFactory<ConnectionsWindow> factory) {
        super(factory);
    }

    @Override
    public void onShow() {
        get().BackButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnBackButton();
            }
        });
        get().SettingsButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnSettingsButton();
            }
        });
    }

    @Override
    public void Execute() {

    }

    @Override
    public void OnHide() {

    }

    @Override
    public EWindowType getWindowType() {
        return EWindowType.Connections;
    }

    private void OnBackButton() {
        WindowService.switchOnPrevious();
    }

    private void OnSettingsButton() {
        WindowService.switchOn(EWindowType.Settings);
    }
}
