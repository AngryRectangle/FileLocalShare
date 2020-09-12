package com.radioactive.gear.project.windows_app.UI.windows.qr_code;

import com.radioactive.gear.project.core.Debug;
import com.radioactive.gear.project.windows_app.UI.AWindow;
import com.radioactive.gear.project.windows_app.UI.UIResources;

import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class QRCodeWindow extends AWindow {
    public Button SettingsButton;
    public Button BackButton;
    public ImageView QRImage;
    private Parent _parent;

    public QRCodeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(UIResources.QR_CODE_WINDOW));
            _parent = loader.load();
            ObservableMap<String, Object> namespace = loader.getNamespace();

            SettingsButton = (Button) namespace.get("settingsButton");
            BackButton = (Button) namespace.get("backButton");
            QRImage = (ImageView) namespace.get("qrImage");
        } catch (IOException e) {
            Debug.error(e.toString());
        }
    }

    @Override
    public Parent getView() {
        return _parent;
    }
}
