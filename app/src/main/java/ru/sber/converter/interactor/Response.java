package ru.sber.converter.interactor;

import android.support.annotation.Nullable;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class Response<T> {
    private T mBody;
    private Throwable mError;

    public Response(T body, Throwable t) {
        this.mBody = body;
        this.mError = t;
    }

    public Response(T body) {
        this.mBody = body;
    }

    public Response(Throwable t) {
        this.mError = t;
    }

    public @Nullable Throwable getError() {
        return mError;
    }

    public @Nullable T getBody() {
        return mBody;
    }
}
