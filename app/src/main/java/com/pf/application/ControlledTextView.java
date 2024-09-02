package com.pf.application;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;


public class ControlledTextView extends androidx.appcompat.widget.AppCompatTextView {
    final boolean horizontality;

    public ControlledTextView(Context context) {
        super(context);
        horizontality =true;
    }

    public ControlledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ControlledImageButton,
                0, 0);

        try {

            horizontality = a.getInteger(R.styleable.ControlledImageButton_orientation,0)==0;
        } finally {
            a.recycle();
        }
    }

    public ControlledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ControlledImageButton,
                0, 0);

        try {

            horizontality = a.getInteger(R.styleable.ControlledImageButton_orientation,0)==0;
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       if(horizontality)
           super.onMeasure(heightMeasureSpec, heightMeasureSpec);
       else
           super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
