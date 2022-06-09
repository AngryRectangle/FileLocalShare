package com.radioactive.gear.project.windows_app;

import com.radioactive.gear.project.windows_app.UI.EWindowType;

import java.util.Stack;

public class AppContext {
    public Stack<EWindowType> windowStack = new Stack<>();
    public byte[] uniqueCode;
}
