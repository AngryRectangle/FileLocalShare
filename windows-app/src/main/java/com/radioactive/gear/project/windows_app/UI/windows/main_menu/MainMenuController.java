package com.radioactive.gear.project.windows_app.UI.windows.main_menu;

import com.radioactive.gear.project.windows_app.UI.AWindowController;
import com.radioactive.gear.project.windows_app.UI.EWindowType;
import com.radioactive.gear.project.windows_app.UI.IWindowFactory;
import com.radioactive.gear.project.windows_app.UI.WindowService;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MainMenuController extends AWindowController<MainMenuWindow> {
    public MainMenuController(IWindowFactory<MainMenuWindow> factory) {
        super(factory);
    }

    @Override
    public void onShow() {
        get().ConnectButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnConnectButton();
            }
        });
        get().SettingsButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnSettingsButton();
            }
        });
        get().AppName.setText("FileLocalShare");
    }

    @Override
    public void Execute() {

    }

    @Override
    public void OnHide() {

    }

    @Override
    public EWindowType getWindowType() {
        return EWindowType.MainMenu;
    }

    private void OnSettingsButton() {
        WindowService.switchOn(EWindowType.Settings);
    }

    private void OnConnectButton() {
        WindowService.switchOn(EWindowType.Connections);
    }
}
