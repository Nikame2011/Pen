package com.pf.application;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;


public class ControlledImageView extends androidx.appcompat.widget.AppCompatImageView {
    final boolean horizontality;

    public ControlledImageView(Context context) {
        super(context);
        horizontality =true;
    }

    public ControlledImageView(Context context, AttributeSet attrs) {
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

    public ControlledImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
