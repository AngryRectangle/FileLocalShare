package com.radioactiv_gear_project.windows_app.UI;

public abstract class AWindowController<TWindow extends Window> {
    private TWindow _instance;
    private IWindowFactory<TWindow> _factory;

    public AWindowController(IWindowFactory<TWindow> factory) {
        _factory = factory;
    }

    public abstract void OnShow();

    public abstract void Execute();

    public abstract void OnHide();

    public abstract EWindowType getWindowType();

    protected TWindow Get() {
        if (_instance == null)
            _instance = _factory.instantiate();
        return _instance;
    }
}
