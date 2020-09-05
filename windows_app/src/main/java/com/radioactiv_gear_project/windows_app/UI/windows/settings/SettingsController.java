package com.radioactiv_gear_project.windows_app.UI.windows.settings;

import com.radioactiv_gear_project.windows_app.UI.AWindowController;
import com.radioactiv_gear_project.windows_app.UI.EWindowType;
import com.radioactiv_gear_project.windows_app.UI.IWindowFactory;
import com.radioactiv_gear_project.windows_app.UI.WindowService;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class SettingsController extends AWindowController<SettingsWindow> {

    public SettingsController(IWindowFactory<SettingsWindow> factory) {
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
    }

    @Override
    public void Execute() {

    }

    @Override
    public void OnHide() {

    }

    @Override
    public EWindowType getWindowType() {
        return EWindowType.Settings;
    }

    private void OnBackButton(){
        WindowService.switchOnPrevious();
    }
}
