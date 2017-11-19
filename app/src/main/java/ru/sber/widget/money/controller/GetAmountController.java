package ru.sber.widget.money.controller;

import android.support.annotation.Nullable;

/**
 * Created by vkirillov on 31.07.2017.
 */

public class GetAmountController {

    public @Nullable Double amountToDouble(String text) {
        try {
            return Double.valueOf(text);
        } catch (Throwable t) {
            return null;
        }
    }
}
