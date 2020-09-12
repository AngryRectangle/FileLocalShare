package com.radioactive.gear.project.windows_app.UI;

import java.util.ArrayList;
import java.util.List;

public class ViewControllerService {
    private static List<ViewPair> _controllers = new ArrayList<>();

    public static void addController(EViewType type, AViewController controller){
        _controllers.add(new ViewPair(controller, type));
    }

    public static AViewController getController(EViewType type){
        for(int i = 0; i < _controllers.size(); i++){
            ViewPair pair = _controllers.get(i);
            if(pair.Type == type)
                return pair.Controller;
        }
        throw new RuntimeException("No controller for view type "+type);
    }

    private static class ViewPair{
        public AViewController Controller;
        public EViewType Type;

        public ViewPair(AViewController controller, EViewType type) {
            Controller = controller;
            Type = type;
        }
    }
}
