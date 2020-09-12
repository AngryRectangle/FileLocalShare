package com.radioactive.gear.project.windows_app.UI.windows.connections;

import com.radioactive.gear.project.windows_app.UI.*;
import com.radioactive.gear.project.windows_app.UI.views.connection.ConnectionInfo;
import com.radioactive.gear.project.windows_app.UI.views.connection.ConnectionView;
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
        for (int i = 0; i < 20; i++)
            addTestDevices();
    }

    @Override
    public void Execute() {

    }

    @Override
    public void OnHide() {
        get().DevicesBox.getChildren().clear();
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

    private void addTestDevices() {
        /*ConnectionView view = new ConnectionView();
        ViewControllerService.getController(EViewType.Connection).onShow(view, new ConnectionInfo("SomePC"));
        get().DevicesBox.getChildren().add(view.getView());*/
    }
}
