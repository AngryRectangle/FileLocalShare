package com.radioactiv_gear_project.windows_app.UI.windows.qr_code;

import com.radioactiv_gear_project.windows_app.UI.AWindowController;
import com.radioactiv_gear_project.windows_app.UI.EWindowType;
import com.radioactiv_gear_project.windows_app.UI.IWindowFactory;
import com.radioactiv_gear_project.windows_app.UI.WindowService;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class QRCodeController extends AWindowController<QRCodeWindow> {
    public QRCodeController(IWindowFactory<QRCodeWindow> factory) {
        super(factory);
    }

    @Override
    public void onShow() {
        get().BackButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onBackButton();
            }
        });

        get().SettingsButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onSettingsButton();
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
        return EWindowType.QRCodeWindow;
    }

    private void onBackButton(){
        WindowService.switchOnPrevious();
    }

    private void onSettingsButton(){
        WindowService.switchOn(EWindowType.Settings);
    }
}
