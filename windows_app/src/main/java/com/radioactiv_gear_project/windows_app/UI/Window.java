package com.radioactiv_gear_project.windows_app.UI;

import com.radioactiv_gear_project.windows_app.Main;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class Window {
    public void show(){
        Parent group = getView();
        Stage stage = Main.stage;
        stage.getScene().setRoot(group);
        Main.stage.show();
    }
    protected abstract Parent getView();
}
