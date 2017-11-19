package ru.sber.widget.money.decor;

import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class SberDecorator {

    private final String mThousandsSeparator;
    private final String mDecimalSeparator;

    public SberDecorator(String thousandsSeparator, String decimalSeparator) {
        mThousandsSeparator = thousandsSeparator;
        mDecimalSeparator = decimalSeparator;
    }

    public void decor(Editable editable, String text, int selection) {
        if (TextUtils.isEmpty(text)) {
            editable.replace(0, editable.length(), "");
            return;
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        decorNumber(spannableStringBuilder);
        editable.replace(0, editable.length(), spannableStringBuilder);
        if (selection > editable.length()) {
            selection = editable.length();
        }
        Selection.setSelection(editable, selection);
    }

    private void decorNumber(SpannableStringBuilder spannableStringBuilder) {
        int decimalSeparatorIndex = -1;

        for (int i = 0; i < spannableStringBuilder.length(); i++) {
            char c = spannableStringBuilder.charAt(i);
            if (c == '.') {
                decimalSeparatorIndex = i;
                break;
            }
        }

        if (decimalSeparatorIndex == -1) {
            // Нет разделителя
            if (spannableStringBuilder.length() <= 3) {
                return;
            }

            for (int i = 0; i < spannableStringBuilder.length(); i++) {
                final int invertIndex = spannableStringBuilder.length() - i - 1;
                if ((invertIndex + 1) % 3 == 0) {
                    final int realIndex = spannableStringBuilder.length() - invertIndex - 1;
                    if (realIndex != 0) {
                        spannableStringBuilder.setSpan(new DecoratorSpan(mThousandsSeparator, ""),
                                realIndex, realIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            return;
        }

        // Разделитель есть
        spannableStringBuilder.setSpan(new ReplaceSpan(mDecimalSeparator), decimalSeparatorIndex,
                decimalSeparatorIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence reversed = new StringBuilder(spannableStringBuilder).reverse();
        boolean separatorPassed = false;
        int separatorIndex = 0;

        for (int i = 0; i < reversed.length(); i++) {
            if (!separatorPassed) {
                char c = reversed.charAt(i);
                if (".".equals(String.valueOf(c))) {
                    separatorPassed = true;
                    separatorIndex = i;
                }
            } else {
                int invertIndex = i - separatorIndex - 1;
                if ((invertIndex + 1) % 3 == 0) {
                    final  int realInvertIndex = invertIndex + separatorIndex + 1;
                    final int realIndex = spannableStringBuilder.length() - realInvertIndex - 1;
                    if (realIndex != 0) {
                        spannableStringBuilder.setSpan(new DecoratorSpan(mThousandsSeparator, ""),
                                realIndex, realIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
    }
}
