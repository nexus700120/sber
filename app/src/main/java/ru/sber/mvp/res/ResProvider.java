package ru.sber.mvp.res;

import android.support.annotation.StringRes;

/**
 * Created by Vitaly on 19.11.2017.
 */

public interface ResProvider {

    String string(@StringRes int resId);
}
