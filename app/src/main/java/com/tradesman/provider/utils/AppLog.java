package com.tradesman.provider.utils;

import android.util.Log;

/**
 * Created by Elluminati Mohit on 1/13/2017.
 */

public class AppLog {

    private static final boolean isDebug = true;

    public static  void Log(String TAG,String message){
        if (isDebug){
            Log.i(TAG,message);
        }
    }
}
