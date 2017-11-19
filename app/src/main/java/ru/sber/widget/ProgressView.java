package ru.sber.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.sber.converter.R;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class ProgressView extends FrameLayout {

    public interface OnRetryListener {
        void onRetry();
    }

    private ViewGroup mErrorContainer;
    private TextView mErrorMessage;
    private Button mRetryButton;
    private ProgressBar mProgressBar;

    public ProgressView(@NonNull Context context) {
        super(context);
        init();
    }

    public ProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.progress_view, this, true);

        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(GONE);

        mErrorContainer = findViewById(R.id.error_container);
        mErrorMessage = mErrorContainer.findViewById(R.id.error_message);
        mRetryButton = mErrorContainer.findViewById(R.id.retry);

        mErrorContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mErrorContainer.getLayoutParams();
        lp.topMargin = (int) (h * 0.2);
    }

    public void error(String message, final OnRetryListener listener) {
        mErrorContainer.setVisibility(VISIBLE);

        List<View> goneViewList = getViewList(mErrorContainer);
        for (View view: goneViewList) {
            view.setVisibility(GONE);
        }
        mErrorMessage.setText(message);
        mRetryButton.setOnClickListener(listener == null ? null : new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRetry();
            }
        });
    }

    public void showProgress() {
        List<View> viewList = getViewList(mProgressBar);
        mProgressBar.setVisibility(VISIBLE);

        for (View view: viewList) {
            view.setVisibility(GONE);
        }
    }

    public void hideProgress() {
        List<View> viewList = getViewList(mErrorContainer, mProgressBar);

        mProgressBar.setVisibility(GONE);
        for (View view: viewList) {
            view.setVisibility(VISIBLE);
        }
    }

    private List<View> getViewList(View... excludedViews) {
        List<View> excludedViewList = Arrays.asList(excludedViews);
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null && !excludedViewList.contains(child)) {
                viewList.add(child);
            }
        }
        return viewList;
    }
}
