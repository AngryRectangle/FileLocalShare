package com.radioactiv_gear_project.windows_app.UI.views.connection;

import com.radioactiv_gear_project.windows_app.UI.AViewController;
import com.radioactiv_gear_project.windows_app.UI.EWindowType;
import com.radioactiv_gear_project.windows_app.UI.WindowService;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ConnectionController extends AViewController<ConnectionView, ConnectionInfo> {

    @Override
    public void onShow(ConnectionView view, ConnectionInfo connectionInfo) {
        view.DeviceNameText.setText(connectionInfo.deviceName);
        view.ConnectButton.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnConnectButton();
            }
        });
    }

    @Override
    public void onHide(ConnectionView view, ConnectionInfo connectionInfo) {

    }

    private void OnConnectButton(){
        WindowService.switchOn(EWindowType.QRCodeWindow);
    }
}
