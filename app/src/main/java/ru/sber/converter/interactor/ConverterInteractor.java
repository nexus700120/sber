package ru.sber.converter.interactor;

import ru.sber.converter.domain.RatesResponse;
import ru.sber.mvp.InteractorCallback;

/**
 * Created by Vitaly on 19.11.2017.
 */

public interface ConverterInteractor {

    void unsubscribe();

    void getRates(InteractorCallback<RatesResponse> callback);
}
