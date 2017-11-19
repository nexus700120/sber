package ru.sber.mvp;

/**
 * Created by Vitaly on 19.11.2017.
 */

public interface InteractorCallback<T> {
    void onSuccess(T result);
    void onError(Throwable t);
}
