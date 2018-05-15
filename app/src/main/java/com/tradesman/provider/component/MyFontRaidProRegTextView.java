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
 * Created by Elluminati Mohit on 1/23/2017.
 */

public class MyFontRaidProRegTextView extends TextView {


    private Typeface typeface;

    public MyFontRaidProRegTextView(Context context) {
        super(context);
    }

    public MyFontRaidProRegTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public MyFontRaidProRegTextView(Context context, AttributeSet attrs, int defStyle) {
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
                        "fonts/MyriadPro-Regular.otf");
            }

        } catch (Exception e) {
            AppLog.Log(Const.TAG, Const.EXCEPTION+e);
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}
