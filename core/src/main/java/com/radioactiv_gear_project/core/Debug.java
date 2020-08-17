package com.radioactiv_gear_project.core;

public final class Debug {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void log(String value, boolean needStacktrace){
        StringBuilder builder = new StringBuilder(value);
        builder.append('\n');
        if(needStacktrace)
            builder.append(getStackTrace());
        System.out.print(builder.toString());
    }
    public static void warning(String value){
        System.out.print(ANSI_YELLOW + value + '\n' + getStackTrace());
    }
    public static void error(String value){
        System.out.print(ANSI_RED + value + '\n' + getStackTrace());
    }
    private static String getStackTrace(){
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StringBuilder builder = new StringBuilder("Stacktrace: ");
        builder.append('\n');
        for(int i = 3; i<stack.length; i++) {
            builder.append(stack[i]);
            builder.append('\n');
        }
        return builder.toString();
    }
}
