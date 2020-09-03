package com.radioactiv_gear_project.windows_app.UI;

import com.radioactiv_gear_project.windows_app.Main;

public class WindowService {
    private AWindowController[] controllers;

    public WindowService(AWindowController... controllers) {
        this.controllers = controllers;
    }

    public void Open(EWindowType windowType){
        for(int i = 0; i < controllers.length; i++){
            AWindowController controller = controllers[i];
            if(controller.getWindowType() != windowType)
                continue;

            AWindowController previous = Main.appContext.currentController;
            if(previous!=null)
                previous.OnHide();
            Main.appContext.previousController = Main.appContext.currentController;
            Main.appContext.currentController = controller;
            controller.Get().show();
            controller.OnShow();
        }
    }
}
