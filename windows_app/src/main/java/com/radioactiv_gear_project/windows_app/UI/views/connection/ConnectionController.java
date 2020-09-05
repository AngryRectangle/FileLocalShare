package com.radioactiv_gear_project.windows_app.UI.views.connection;

import com.radioactiv_gear_project.windows_app.UI.AViewController;

public class ConnectionController extends AViewController<ConnectionView, ConnectionInfo> {

    @Override
    public void onShow(ConnectionView view, ConnectionInfo connectionInfo) {
        view.DeviceNameText.setText(connectionInfo.deviceName);
    }

    @Override
    public void onHide(ConnectionView view, ConnectionInfo connectionInfo) {

    }
}
