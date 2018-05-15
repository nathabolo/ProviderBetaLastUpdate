package com.tradesman.provider.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.tradesman.provider.utils.AppLog;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.utils.Const;

/**
 * Created by Elluminati Mohit on 1/23/2017.
 */

public class MyriadProRegButton extends Button {


    private Typeface typeface;

    public MyriadProRegButton(Context context) {
        super(context);
    }

    public MyriadProRegButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public MyriadProRegButton(Context context, AttributeSet attrs, int defStyle) {
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
