package ru.sber.converter.domain;

import android.support.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class MoneyFormatter {

    @Nullable
    public String format(Double money) {
        if (money == null) {
            return null;
        }
        return new DecimalFormat("###,###,###,##0.00").format(money);
    }

    public String format(Double money, int precision) {
        if (money == null) {
            return null;
        }
        StringBuilder formatPattern = new StringBuilder("###,###,###,##0");
        if (precision > 0) {
            formatPattern.append(".");
            for (int i = precision; i > 0; i--) {
                formatPattern.append("0");
            }
        }
        return new DecimalFormat(formatPattern.toString()).format(money);
    }

    @Nullable
    public String format(Double money, String currency, int precision) {
        if (money == null || currency == null || currency.isEmpty()) {
            return null;
        }
        String result = null;
        try {
            NumberFormat numberFormat =
                    NumberFormat.getCurrencyInstance(Locale.getDefault());
            numberFormat.setCurrency(Currency.getInstance(currency.toUpperCase()));
            numberFormat.setMinimumFractionDigits(precision);
            numberFormat.setMaximumFractionDigits(precision);
            result = numberFormat.format(money);
        } catch (Exception e) {
            result = String.format("%s %s", format(money, precision), currency.toUpperCase());
        }
        return replaceRussianRublesToOwn(result);
    }

    @Nullable
    public String format(Double money, String currency) {
        return format(money, currency, 2);
    }


    private static String replaceRussianRublesToOwn(String text) {
        int indexEnUs = text.indexOf("RUB");
        int indexRu = text.indexOf("руб.");
        if (indexEnUs >= 0) {
            return text.replace("RUB", "\u20BD");
        }
        if (indexRu >= 0) {
            return text.replace("руб.", "\u20BD");
        }
        return text;
    }
}
