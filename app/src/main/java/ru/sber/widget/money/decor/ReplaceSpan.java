package ru.sber.widget.money.decor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by Vitaly on 18.11.2017.
 */

public class ReplaceSpan extends ReplacementSpan {

    private final String mReplaceText;

    public ReplaceSpan(String replaceText) {
        mReplaceText = replaceText;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence charSequence, int i, int i1,
                       @Nullable Paint.FontMetricsInt fontMetricsInt) {
        return Math.round(paint.measureText(mReplaceText, 0, mReplaceText.length()));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence charSequence, int i, int i1, float v,
                     int i2, int i3, int i4, @NonNull Paint paint) {
        canvas.drawText(mReplaceText, v, (float) i3, paint);
    }
}
