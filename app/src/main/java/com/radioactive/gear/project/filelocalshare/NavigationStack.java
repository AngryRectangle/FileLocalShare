package com.radioactive.gear.project.filelocalshare;

import java.util.Stack;

public final class NavigationStack {
    private Stack _stack = new Stack<>();

    public void put(int view) {
        _stack.add(view);
    }

    public int pop() {
        return (int) _stack.pop();
    }
}
