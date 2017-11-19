package ru.sber.mvp;

/**
 * Created by Vitaly on 18.11.2017.
 */

public interface MvpPresenter<T> {

    void attachView(T view);

    void detachView();
}
