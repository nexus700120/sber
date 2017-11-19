package ru.sber.converter.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.sber.converter.R;
import ru.sber.converter.ConverterContract;
import ru.sber.converter.domain.RatesResponse;
import ru.sber.converter.interactor.ConverterInteractorImpl;
import ru.sber.converter.presenter.ConverterPresenter;
import ru.sber.mvp.res.ResProviderImpl;
import ru.sber.widget.ProgressView;
import ru.sber.widget.money.MoneyEditText;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class ConverterFragment extends Fragment implements ConverterContract.View {

    @SuppressWarnings("ConstantConditions")
    private ConverterContract.Presenter mPresenter =
            new ConverterPresenter(new ConverterInteractorImpl());

    private ProgressView mProgressView;
    private TextView mOutgoingCurrency;
    private TextView mRateView;
    private MoneyEditText mMoneyEditText;
    private TextView mIncomingAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.setResProvider(new ResProviderImpl(view.getContext()));

        mMoneyEditText = view.findViewById(R.id.money_edit_text);
        mMoneyEditText.setOnAmountChangeListener(new MoneyEditText.OnAmountChangedListener() {
            @Override
            public void onAmountChanged(@Nullable Double amount) {
                mPresenter.onOutgoingAmountChanged(amount);
            }
        });
        mProgressView = view.findViewById(R.id.progress_view);
        mOutgoingCurrency = view.findViewById(R.id.outgoing_currency);
        mOutgoingCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onOutgoingCurrencyClicked();
            }
        });
        mRateView = view.findViewById(R.id.rate);
        mIncomingAmount = view.findViewById(R.id.incoming_amount);

        mPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showProgress() {
        mProgressView.showProgress();
    }

    @Override
    public void hideProgress() {
        mProgressView.hideProgress();
    }

    @Override
    public void error(String message, ProgressView.OnRetryListener listener) {
        mProgressView.error(message, listener);
    }

    @Override
    public void bindOutgoingCurrency(String outgoingCurrency) {
        mOutgoingCurrency.setText(outgoingCurrency);
    }

    @Override
    public void bindRate(String rate) {
        mRateView.setText(rate);
    }

    @Override
    public void showCurrencyListDialog(final List<RatesResponse.CurrencyInfo> currencyInfoList) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        final CharSequence itemsArray[] = new CharSequence[currencyInfoList.size()];
        for (int i = 0; i < currencyInfoList.size(); i++) {
            RatesResponse.CurrencyInfo currencyInfo = currencyInfoList.get(i);
            itemsArray[i] = String.format("%s(%s)", currencyInfo.name, currencyInfo.charCode);
        }
        new AlertDialog.Builder(activity)
                .setItems(itemsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RatesResponse.CurrencyInfo currencyInfo = currencyInfoList.get(i);
                        mPresenter.onCurrencySelected(currencyInfo);
                    }
                }).show();
    }

    @Override
    public Double getOutgoingAmount() {
        return mMoneyEditText.getAmount();
    }

    @Override
    public void setIncomingAmount(String amount) {
        mIncomingAmount.setText(amount);
    }
}
