package ru.sber.widget.money.controller;

import android.support.v4.util.Pair;

/**
 * Created by vkirillov on 28.07.2017.
 */
public class DeleteController {

    public Pair<String, Integer> handleDelete(CharSequence s, String beforeText, int beforeSelection,
                                              String currentText, int currentSelection) {
        if (!currentText.contains(".") && currentText.length() > 10) {
            return new Pair<>(beforeText, beforeSelection);
        }

        if (currentText.isEmpty()) {
            return new Pair<>("", 0);
        }

        if (currentText.startsWith(".")) {
            return new Pair<>("0" + currentText, currentSelection);
        }
        return new Pair<>(currentText, currentSelection);
    }
}