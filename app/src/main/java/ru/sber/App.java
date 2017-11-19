package ru.sber;

import android.app.Application;
import android.content.Context;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class App extends Application {

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }

    public static Context getAppContext() {
        return sAppContext;
    }
}
