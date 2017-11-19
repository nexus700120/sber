package ru.sber.mvp.res;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class ResProviderImpl implements ResProvider {

    private final Context mContext;

    public ResProviderImpl(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public String string(int resId) {
        return mContext.getString(resId);
    }
}
