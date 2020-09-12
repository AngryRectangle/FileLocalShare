package com.radioactive.gear.project.windows_app.UI;

public abstract class AWindowController<TWindow extends AWindow> {
    private TWindow _instance;
    private IWindowFactory<TWindow> _factory;

    public AWindowController(IWindowFactory<TWindow> factory) {
        _factory = factory;
    }

    public abstract void onShow();

    public abstract void Execute();

    public abstract void OnHide();

    public abstract EWindowType getWindowType();

    protected TWindow get() {
        if (_instance == null)
            _instance = _factory.instantiate();
        return _instance;
    }
}
