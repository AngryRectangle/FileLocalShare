package com.radioactive.gear.project.windows_app.UI;

import com.radioactive.gear.project.windows_app.AppContext;

public final class WindowService {
    private static AWindowController[] _controllers;
    private static AppContext _appContext;

    public static void initialize(AppContext appContext, AWindowController... controllers) {
        _controllers = controllers;
        _appContext = appContext;
    }

    public static AWindowController getController(EWindowType windowType) {
        for (int i = 0; i < _controllers.length; i++) {
            AWindowController controller = _controllers[i];
            if (controller.getWindowType() == windowType)
                return controller;
        }

        throw new RuntimeException("No controller for window type " + windowType);
    }

    public static void switchOn(EWindowType windowType) {
        closeCurrent();
        _appContext.windowStack.add(windowType);
        openCurrent();
    }

    public static void switchOnPrevious() {
        closeCurrent();
        _appContext.windowStack.pop();
        openCurrent();
    }
    public static void closeCurrent() {
        EWindowType currentType = _appContext.windowStack.size() > 0 ? _appContext.windowStack.peek() : null;
        if (currentType == null)
            return;

        getController(currentType).OnHide();
    }

    public static void openCurrent() {
        EWindowType currentType = _appContext.windowStack.size() > 0 ? _appContext.windowStack.peek() : null;
        if (currentType == null)
            return;

        AWindowController newController = getController(currentType);
        newController.get().show();
        newController.onShow();
    }
}
