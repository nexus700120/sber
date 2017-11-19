package ru.sber.converter.presenter;

import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

import ru.sber.converter.ConverterContract;
import ru.sber.converter.R;
import ru.sber.converter.domain.MoneyFormatter;
import ru.sber.converter.domain.RatesResponse;
import ru.sber.converter.interactor.ConverterInteractor;
import ru.sber.mvp.res.ResProvider;
import ru.sber.mvp.InteractorCallback;
import ru.sber.widget.ProgressView;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class ConverterPresenter implements ConverterContract.Presenter {

    private final ConverterInteractor mInteractor;
    private final MoneyFormatter mMoneyFormatter = new MoneyFormatter();
    private ConverterContract.View mView;
    private ResProvider mResProvider;

    private List<RatesResponse.CurrencyInfo> mCurrencyInfoList;
    private RatesResponse.CurrencyInfo mCurrentCurrencyInfo;

    public ConverterPresenter(ConverterInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void attachView(final ConverterContract.View view) {
        mView = view;
        retry();
    }

    private void retry() {
        mView.showProgress();
        mInteractor.getRates(new InteractorCallback<RatesResponse>() {
            @Override
            public void onSuccess(RatesResponse result) {
                ConverterPresenter.this.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                ConverterPresenter.this.onError(t);
            }
        });
    }

    private void onSuccess(RatesResponse result) {
        if (mView == null) {
            return;
        }
        if (result == null || result.currencyInfoList == null || result.currencyInfoList.isEmpty()) {
            onError(new NullPointerException());
            return;
        }

        mView.hideProgress();
        mCurrencyInfoList = result.currencyInfoList;
        mCurrentCurrencyInfo = mCurrencyInfoList.get(0);
        updateInfoAboutCurrency();
    }

    private void onError(Throwable throwable) {
        if (mView == null) {
            return;
        }
        mView.error(mResProvider.string(R.string.common_error_unknown),
                new ProgressView.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        retry();
                    }
                });
    }

    private void updateInfoAboutCurrency() {
        mView.bindOutgoingCurrency(mCurrentCurrencyInfo.charCode);

        String rate = String.format("%s = %s",
                mMoneyFormatter.format(1.0, mCurrentCurrencyInfo.charCode, 0),
                mMoneyFormatter.format(mCurrentCurrencyInfo.value, "RUB", 4));
        mView.bindRate(rate);

        onOutgoingAmountChanged(mView.getOutgoingAmount());
    }

    @Override
    public void detachView() {
        mView = null;
        mInteractor.unsubscribe();
    }


    @Override
    public void setResProvider(ResProvider resProvider) {
        mResProvider = resProvider;
    }

    @Override
    public void onOutgoingCurrencyClicked() {
        if (mCurrencyInfoList == null) {
            return;
        }
        mView.showCurrencyListDialog(mCurrencyInfoList);
    }

    @Override
    public void onCurrencySelected(RatesResponse.CurrencyInfo currencyInfo) {
        mCurrentCurrencyInfo = currencyInfo;
        updateInfoAboutCurrency();
    }

    @Override
    public void onOutgoingAmountChanged(@Nullable Double amount) {
        if (amount == null || amount == 0.0) {
            mView.setIncomingAmount("0");
        } else {
            Double result = new BigDecimal(amount)
                    .multiply(new BigDecimal(mCurrentCurrencyInfo.value)).doubleValue();
            mView.setIncomingAmount(mMoneyFormatter.format(result));
        }
    }
}
