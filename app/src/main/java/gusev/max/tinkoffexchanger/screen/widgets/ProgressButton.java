package gusev.max.tinkoffexchanger.screen.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import gusev.max.tinkoffexchanger.R;

public class ProgressButton extends FrameLayout implements View.OnTouchListener {

    private View background;
    private TextView textView;
    private ProgressBar progressBar;

    public ProgressButton(Context context) {
        super(context);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_progress_button, this);
        background = findViewById(R.id.view_background);
        textView = findViewById(R.id.text_view);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.color_accent_dark), PorterDuff.Mode.SRC_ATOP);

        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        if (typedArray != null) {
            if (typedArray.getString(R.styleable.ProgressButton_text) != null) {
                setText(typedArray.getString(R.styleable.ProgressButton_text));
            }
            setEnabled(typedArray.getBoolean(R.styleable.ProgressButton_enabled, true));
            typedArray.recycle();
        }

        setOnTouchListener(this);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        background.setEnabled(enabled);
        textView.setEnabled(enabled);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void showProgress() {
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        this.setClickable(false);
    }

    public void hideProgress() {
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        this.setClickable(true);
    }
}
