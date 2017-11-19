package ru.sber.util;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class Utils {

    public static void clearFocus(@Nullable Activity activity) {
        if (activity != null) {
            clearFocus(activity.getCurrentFocus());
        }
    }

    public static void clearFocus(@Nullable View view) {
        if (view != null) {
            view.clearFocus();
        }
    }
}
