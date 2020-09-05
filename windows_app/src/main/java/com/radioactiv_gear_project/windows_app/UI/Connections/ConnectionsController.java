package com.radioactiv_gear_project.windows_app.UI.Connections;

import com.radioactiv_gear_project.windows_app.UI.*;
import com.radioactiv_gear_project.windows_app.UI.views.connection.ConnectionInfo;
import com.radioactiv_gear_project.windows_app.UI.views.connection.ConnectionView;
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
        addTestDevices();
        addTestDevices();
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

    private void addTestDevices(){
        ConnectionView view = new ConnectionView();
        ViewControllerService.getController(EViewType.Connection).onShow(view, new ConnectionInfo("SomePC"));
        get().DevicesBox.getChildren().add(view.getView());
    }
}
