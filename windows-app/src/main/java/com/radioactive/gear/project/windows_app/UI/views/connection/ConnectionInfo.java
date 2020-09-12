package com.radioactive.gear.project.windows_app.UI.views.connection;

import com.radioactive.gear.project.windows_app.UI.IViewInfo;

public class ConnectionInfo implements IViewInfo {
    public String deviceName;

    public ConnectionInfo(String deviceName) {
        this.deviceName = deviceName;
    }
}
