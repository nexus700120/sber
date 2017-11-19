package ru.sber.widget.money.decor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by Vitaly on 18.11.2017.
 */
public class DecoratorSpan extends ReplacementSpan {

    private final String mBefore;
    private final String mAfter;

    public DecoratorSpan(String before, String after) {
        mBefore = before;
        mAfter = after;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence charSequence, int i, int i1,
                       @Nullable Paint.FontMetricsInt fontMetricsInt) {
        CharSequence newCharSequence = mBefore + charSequence.subSequence(i, i1) + mAfter;
        return Math.round(paint.measureText(newCharSequence, 0, newCharSequence.length()));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence charSequence, int i, int i1, float v,
                     int i2, int i3, int i4, @NonNull Paint paint) {
        CharSequence newCharSequence = mBefore + charSequence.subSequence(i, i1) + mAfter;
        canvas.drawText(newCharSequence.toString(), v, (float) i3, paint);
    }
}
