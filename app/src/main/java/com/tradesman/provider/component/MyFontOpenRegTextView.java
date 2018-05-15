package com.tradesman.provider.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tradesman.provider.utils.AppLog;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.utils.Const;


/**
 * Created by Akash on 27-12-16.
 */

public class MyFontOpenRegTextView extends TextView {


    private Typeface typeface;

    public MyFontOpenRegTextView(Context context) {
        super(context);
    }

    public MyFontOpenRegTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public MyFontOpenRegTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.app);
        setCustomFont(ctx);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx) {
        try {
            if (typeface == null) {
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        "fonts/OpenSans-Regular.ttf");
            }

        } catch (Exception e) {
            AppLog.Log(Const.TAG, Const.EXCEPTION+e);
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}
