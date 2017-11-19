package ru.sber.widget.money;

import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import ru.sber.widget.money.controller.DeleteController;
import ru.sber.widget.money.controller.GetAmountController;
import ru.sber.widget.money.controller.InputController;
import ru.sber.widget.money.decor.SberDecorator;

/**
 * Created by Vitaly on 18.11.2017.
 */

class MoneyTextWatcher implements TextWatcher {

    private final int INPUT = 1;
    private final int PASTE = 2;
    private final int DELETE = 3;
    private final int UNKNOWN = 4;

    private boolean mSelfChange = false;
    private final Cleaner mCleaner = new Cleaner();
    private final InputController mInputController = new InputController();
    private final DeleteController mDeleteController = new DeleteController();
    private final DecimalFormatSymbols mDecimalFormatSymbols =
            new DecimalFormatSymbols(Locale.getDefault());
    private final SberDecorator mDecorator = new SberDecorator(getThousandSeparator(),
            getDecimalSeparator());
    private final GetAmountController mGetAmountController = new GetAmountController();

    private MoneyEditText.OnAmountChangedListener mListener;

    private String mBeforeText;
    private int mBeforeSelection;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mSelfChange) {
            return;
        }
        Pair<String, Integer> beforePair = mCleaner.clean(charSequence.toString(),
                Selection.getSelectionEnd(charSequence));

        mBeforeText = beforePair.first;
        mBeforeSelection = beforePair.second;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (mSelfChange) {
            return;
        }
        final int operationType = getOperationType(start, before, count);

        if (operationType == INPUT) {
            handleInput(charSequence, start, before, count);
        }
        if (operationType == DELETE) {
            handleDelete(charSequence);
        }

        if (operationType == PASTE) {
            mSelfChange = true;
            ((Editable)charSequence).replace(0, charSequence.length(), "");
            mSelfChange = false;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mSelfChange) {
            return;
        }
        if (mListener != null) {
            mListener.onAmountChanged(mGetAmountController.amountToDouble(editable.toString()));
        }
    }

    private int getOperationType(int start, int before, int count) {
        if (count == 1) {
            return INPUT;
        }
        if (count > 1) {
            return PASTE;
        }
        if (before >= 1) {
            return DELETE;
        }
        return UNKNOWN;
    }

    private String getDecimalSeparator() {
        return String.valueOf(mDecimalFormatSymbols.getDecimalSeparator());
    }

    private String getThousandSeparator() {
        return String.valueOf(mDecimalFormatSymbols.getGroupingSeparator());
    }

    private void handleInput(CharSequence cs, int start, int before, int count) {
        Pair<String, Integer> cleanData = mCleaner.clean(cs.toString(),
                Selection.getSelectionEnd(cs));
        Pair<String, Integer> inputData = mInputController.handleInput(cleanData.first, cleanData.second, mBeforeText,
                mBeforeSelection, cs, start, count);
        mSelfChange = true;
        mDecorator.decor((Editable) cs, inputData.first, inputData.second);
        mSelfChange = false;
    }

    private void handleDelete(CharSequence cs) {
        Pair<String, Integer> cleanData = mCleaner.clean(cs.toString(),
                Selection.getSelectionEnd(cs));
        Pair<String, Integer> deleteData = mDeleteController.handleDelete(cs, mBeforeText, mBeforeSelection,
                cleanData.first, cleanData.second);
        mSelfChange = true;
        mDecorator.decor((Editable) cs, deleteData.first, deleteData.second);
        mSelfChange = false;
    }

    public void setOnAmountChangeListener(MoneyEditText.OnAmountChangedListener listener) {
        mListener = listener;
    }
}
