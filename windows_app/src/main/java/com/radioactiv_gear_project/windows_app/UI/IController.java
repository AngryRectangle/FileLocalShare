package com.radioactiv_gear_project.windows_app.UI;

public interface IController<TView extends Window, TInfo> {
    void OnShow(TView view, TInfo info);
    void Execute(TView view, TInfo info);
    void OnHide(TView view, TInfo info);
}
