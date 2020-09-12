package com.radioactive.gear.project.windows_app.UI.windows.qr_code;

import com.radioactive.gear.project.core.Debug;
import com.radioactive.gear.project.windows_app.UI.AWindowController;
import com.radioactive.gear.project.windows_app.UI.EWindowType;
import com.radioactive.gear.project.windows_app.UI.IWindowFactory;
import com.radioactive.gear.project.windows_app.UI.WindowService;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
/*import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;*/

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        /*File file = QRCode.from("HelloWorld").withSize(128, 128).file();
        try {
            get().QRImage.setImage(new Image(new FileInputStream(file)));
        }catch (IOException e){
            Debug.error(e.toString());
        }*/
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
