package ru.sber.widget.money.controller;


import android.support.v4.util.Pair;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class InputController {

    private final String mDecimalSeparator = ".";

    public Pair<String, Integer> handleInput(String realText, int realSelection, String beforeText,
                                             int beforeSelection, CharSequence cs, int start,
                                             int count) {
        final String input = cs.subSequence(start, start + count).toString();

        return correctText(input, beforeText, beforeSelection,
                realText, realSelection);
    }

    private Pair<String, Integer> correctText(String input, String beforeText, int beforeSelection,
                                              String currentText, int currentSelection) {

        if (isDecimalSeparator(input)) {
            // Ввели разделитель
            if (beforeText.contains(mDecimalSeparator)) {
                // Текст уже содержит разделитель
                return new Pair<>(beforeText, beforeSelection);
            }

            // Проверяем, есть ли числа до разделителя
            int numbersBeforeSeparator = 0;
            for (int i = 0; i < currentText.length(); i++) {
                char c = currentText.charAt(i);
                if (isDecimalSeparator(String.valueOf(c))) {
                    break;
                }
                numbersBeforeSeparator++;
            }

            if (numbersBeforeSeparator == 0) {
                return new Pair<>(beforeText, beforeSelection);
            }

            // Проверяем сколько чисел после разделителя
            int numbersAfterSeparator = 0;
            boolean separatorFound = false;
            for (int i = 0; i < currentText.length(); i++) {
                if (separatorFound) {
                    numbersAfterSeparator++;
                    continue;
                }

                char c = currentText.charAt(i);
                if (isDecimalSeparator(String.valueOf(c))) {
                    separatorFound = true;
                }
            }

            if (numbersAfterSeparator > 2) {
                return new Pair<>(beforeText, beforeSelection);
            }

            return new Pair<>(currentText, currentSelection);
        }

        if (!isNumber(input)) {
            return new Pair<>(beforeText, beforeSelection);
        }

        // Ввели число
        boolean decimalSeparatorExist = currentText.contains(mDecimalSeparator);
        if (!decimalSeparatorExist) {
            // Разделителя вообще нету
            // Это целое число
            int integerNumberCount = 0;
            for (int i = 0; i < currentText.length(); i++) {
                if (isNumber(currentText.substring(i, i + 1))) {
                    integerNumberCount++;
                }
            }

            if (integerNumberCount > 10) {
                return new Pair<>(beforeText, beforeSelection);
            }

            // Ввели ноль
            if ("0".equals(input)) {
                // Если до этого был ноль, то ещё раз ноль ввести нелья
                if ("0".equals(beforeText)) {
                    return new Pair<>(beforeText, beforeSelection);
                }
                if (!beforeText.isEmpty() && currentText.startsWith("0")) {
                    return new Pair<>(beforeText, beforeSelection);
                }
                return new Pair<>(currentText, currentSelection);
            }

            // Ввели не ноль, но до этого был 0
            if ("0".equals(beforeText)) {
                return new Pair<>(input, 1);
            }
            return new Pair<>(currentText, currentSelection);
        }

        // Есть разделитель
        final int decimalSeparatorIndex = currentText.indexOf(mDecimalSeparator);
        if (currentSelection <= decimalSeparatorIndex) {
            // Это целая часть дробного числа
            int integerNumberCount = 0;
            for (int i = 0; i < currentText.length(); i++) {
                String symbol = currentText.substring(i, i + 1);
                if (isDecimalSeparator(symbol)) break;
                if (isNumber(symbol)) integerNumberCount++;
            }
            if (integerNumberCount > 10) {
                return new Pair<>(beforeText, beforeSelection);
            }

            // Ввели ноль
            if ("0".equals(input)) {
                if (currentText.startsWith("0")) {
                    return new Pair<>(beforeText, beforeSelection);
                }
                return new Pair<>(currentText, currentSelection);
            }

            // Ввели не ноль
            if (beforeText.startsWith("0")) {
                final int newSelection = beforeSelection <= 1 ? 1 : 0;
                return new Pair<>(beforeText.replaceFirst("0", input), newSelection);
            }

            return new Pair<>(currentText, currentSelection);
        }

        // Это дробная часть
        int decimalNumberCount = 0;
        boolean separatorFound = false;
        for (int i = 0; i < currentText.length(); i++) {
            if (!separatorFound) {
                String symbol = currentText.substring(i, i + 1);
                if (isDecimalSeparator(symbol)) {
                    separatorFound = true;
                }
            } else decimalNumberCount++;
        }

        if (decimalNumberCount > 2) {
            return new Pair<>(beforeText, beforeSelection);
        }

        return new Pair<>(currentText, currentSelection);
    }

    private boolean isDecimalSeparator(CharSequence cs) {
        return mDecimalSeparator.equals(cs.toString());
    }

    private boolean isNumber(CharSequence cs) {
        return "0123456789".contains(cs);
    }

}