package ru.sber.widget.money;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import ru.sber.widget.money.controller.GetAmountController;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class MoneyEditText extends AppCompatEditText {

    public interface OnAmountChangedListener {
        void onAmountChanged(@Nullable Double amount);
    }

    private final GetAmountController mGetAmountController = new GetAmountController();
    private final MoneyTextWatcher mMoneyTextWatcher = new MoneyTextWatcher();

    public MoneyEditText(Context context) {
        super(context);
        init();
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(mMoneyTextWatcher);
    }

    public Double getAmount() {
        return mGetAmountController.amountToDouble(getText().toString());
    }

    public void setOnAmountChangeListener(OnAmountChangedListener listener) {
        mMoneyTextWatcher.setOnAmountChangeListener(listener);
    }
}
