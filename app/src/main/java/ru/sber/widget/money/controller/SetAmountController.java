package ru.sber.widget.money.controller;

import java.text.DecimalFormat;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class SetAmountController {

    public String amountToString(Double amount) {
        if (amount == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        final String formatted = format(amount);

        // Десятичная часть
        String decimalPart = formatted.substring(formatted.length() - 2, formatted.length());
        if (!"00".equals(decimalPart)) {
            // Дробная часть не нулевая
            if (decimalPart.endsWith("0")) {
                // Если последний символ 0, то мы его опускаем
                decimalPart = decimalPart.substring(0, decimalPart.length() - 1);
            }
            sb.append(decimalPart);
            sb.insert(0, ".");
        }

        // Целую часть берем без изменений
        String integerPart = formatted.substring(0, formatted.length() - 3);
        sb.insert(0, integerPart);

        return sb.toString();

    }

    private String format(Double amount) {
        return new DecimalFormat("0.00").format(amount);
    }
}
