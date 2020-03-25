package com.example.ya.filelocalshare;

public class Converter {
    public static int getPower(long value){
        if(value==0)
            return 0;
        return (int)Math.floor(Math.log10(value)/Math.log10(1024));
    }
    public static float bytesToPower(long bytes, int power){
        return (float) Math.floor((double)bytes/Math.pow(1024, power)*10)/10;
    }
    public static String powerToDesignation(String[] designations, int power){
        if(designations.length<power)
            return designations[designations.length-1];
        //TODO remove this shit
        return designations[power];
    }
}
