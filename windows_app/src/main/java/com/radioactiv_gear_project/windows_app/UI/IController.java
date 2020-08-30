package com.radioactiv_gear_project.windows_app.UI;

import javafx.scene.Group;

public interface IController<TView extends IView, TInfo> {
    void OnShow(TView view, TInfo info);
    void Execute(TView view, TInfo info);
    void OnHide(TView view, TInfo info);
}
