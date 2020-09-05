package com.radioactiv_gear_project.windows_app.UI;

public abstract class AViewController<TView extends AView, TInfo> {
    public abstract void onShow(TView view, TInfo info);
    public abstract void onHide(TView view, TInfo info);
}
