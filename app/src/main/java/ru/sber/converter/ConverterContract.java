package ru.sber.converter;

import android.support.annotation.Nullable;

import java.util.List;

import ru.sber.converter.domain.RatesResponse;
import ru.sber.mvp.MvpPresenter;
import ru.sber.mvp.MvpView;
import ru.sber.mvp.res.ResProvider;
import ru.sber.widget.ProgressView;

/**
 * Created by Vitaly on 18.11.2017.
 */

public interface ConverterContract {

    interface View extends MvpView {
        void showProgress();
        void hideProgress();
        void error(String message, ProgressView.OnRetryListener listener);
        void bindOutgoingCurrency(String outgoingCurrency);
        void bindRate(String rate);
        void showCurrencyListDialog(List<RatesResponse.CurrencyInfo> currencyInfoList);
        Double getOutgoingAmount();
        void setIncomingAmount(String amount);
    }

    interface Presenter extends MvpPresenter<View> {
        void setResProvider(ResProvider resProvider);
        void onOutgoingCurrencyClicked();
        void onCurrencySelected(RatesResponse.CurrencyInfo currencyInfo);
        void onOutgoingAmountChanged(@Nullable Double amount);
    }
}
