package com.radioactive.gear.project.filelocalshare;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class FileLocalShare extends Application {
    private static Activity currentActivity;

    public static Context getActivityContext() {
        return currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                currentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                currentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                // don't clear current activity because activity may get stopped after
                // the new activity is resumed
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // don't clear current activity because activity may get destroyed after
                // the new activity is resumed
            }
        });
    }
}
