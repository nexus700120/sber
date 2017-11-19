package ru.sber.widget.money;


import android.support.v4.util.Pair;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class Cleaner {

    public Pair<String, Integer> clean(String text, int selection) {
        if (text.isEmpty()) {
            return new Pair<>("", 0);
        }

        final StringBuilder sb = new StringBuilder();

        final String reversed = new StringBuilder(text).reverse().toString();
        int reversedSelection = text.length() - selection;
        boolean separatorAlreadyAdded = false;

        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (c == '.' && !separatorAlreadyAdded) {
                sb.append(c);
                separatorAlreadyAdded = true;
            } else if ("0123456789".contains(String.valueOf(c))) {
                sb.append(c);
            } else if (i >= reversedSelection) {
                reversedSelection++;
            }
        }

        final int realSelection = text.length() - reversedSelection;
        return new Pair<>(sb.reverse().toString(), realSelection);
    }
}
