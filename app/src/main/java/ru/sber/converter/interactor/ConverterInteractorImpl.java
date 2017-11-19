package ru.sber.converter.interactor;

import ru.sber.converter.domain.RatesResponse;
import ru.sber.mvp.InteractorCallback;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class ConverterInteractorImpl implements ConverterInteractor {

    private GetRatesAsyncTask mAsyncTask;

    @Override
    public void unsubscribe() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(false);
        }
    }

    @Override
    public void getRates(InteractorCallback<RatesResponse> callback) {
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(false);
        }
        mAsyncTask = new GetRatesAsyncTask(callback);
        mAsyncTask.execute();
    }

}
