package com.radioactive.gear.project.windows_app.UI.views.connection;

import com.radioactive.gear.project.core.Debug;
import com.radioactive.gear.project.windows_app.UI.AView;
import com.radioactive.gear.project.windows_app.UI.UIResources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class ConnectionView extends AView {
    public Button ConnectButton;
    public Text DeviceNameText;
    private Parent _parent;
    public ConnectionView(){
        try {
            _parent = FXMLLoader.load(getClass().getResource(UIResources.CONNECTION_VIEW));
        } catch (IOException e) {
            Debug.error(e.toString());
        }

        ConnectButton = (Button) _parent.lookup("#connectButton");
        DeviceNameText = (Text) _parent.lookup("#deviceNameText");
    }
    @Override
    public Parent getView() {
        return _parent;
    }
}
