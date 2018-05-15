package com.tradesman.provider.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jimmiejobscreative.tradesman.provider.R;;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Elluminati Mohit on 1/5/2017.
 */

public class Andyutils {

    private static ProgressDialog mProgressDialog;
    private static AlertDialog gpsDialog;
    private static final String EXCEPTION = "Exception";
    private static final String TAG = "tag";
    private static Dialog customDialog;

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo!= null && networkInfo.isConnectedOrConnecting();

    }

    public static boolean isGPSAvailable(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
         return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }



    public static void showErrorToast(int id, Context ctx) {
        String msg;
        String errorCode = Const.ERROR_CODE_PREFIX + id;

        msg = ctx.getResources().getString(
                ctx.getResources().getIdentifier(errorCode, "string",
                        ctx.getPackageName()));
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean hasAnyPrifix(String number, String... prefixes){
        if (number == null) {
            return false;
        }
        for (String prefix : prefixes) {
            if (number.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }


    public static void showSimpleProgressDialog(Activity activity, String title, String message, boolean isCancelable){

        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(activity, title, message);
                mProgressDialog.setCancelable(isCancelable);
            }
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            hideKeybord(activity);
        }catch (IllegalArgumentException ie) {
            AppLog.Log(TAG,EXCEPTION+ie);
        }

    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;

            }
        } catch (IllegalArgumentException ie) {
            AppLog.Log(TAG,EXCEPTION+ie);
        }
    }



    public  static void showGPSErrorDialog(final Activity activity){

        if(!gpsDialog.isShowing()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(activity.getString(R.string.dialog_no_gps))
                    .setMessage(activity.getString(R.string.dialog_no_gps_message))
                    .setPositiveButton(activity.getString(R.string.dialog_enable_gps), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            removeGpsDialog();
                        }
                    })
                    .setNegativeButton(activity.getString(R.string.dialog_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeGpsDialog();
                            activity.finish();
                        }
                    });

            gpsDialog = alertDialog.create();
            gpsDialog.show();
        }



    }

    public static void removeGpsDialog(){

        if (gpsDialog != null && gpsDialog.isShowing()) {
            gpsDialog.dismiss();
            gpsDialog = null;
        }

    }
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


    public static String showColor(int id , Context context){
        String msg;
        String colorCode = "color_"+id;

        msg = context.getResources().getString(
                context.getResources().getIdentifier(colorCode, "color",
                        context.getPackageName()));

        return msg;
    }


    public static String showListColor(int id, Context context){

        String msg;
        String colorCode = "color_list_"+id;

        msg = context.getResources().getString(
                context.getResources().getIdentifier(colorCode, "color",
                        context.getPackageName()));

        return msg;
    }

    public static String currencySigns(String data){

        if(TextUtils.equals("USD",data)){
            return "$";
        }
        return "$";
    }


    public static String getTimeZone(){

        return java.util.TimeZone.getDefault().getID();

    }

    public static String formateDate(String date, boolean isOnlyHours){

        Date myDate = null;
            try {
                SimpleDateFormat oldFormate = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                myDate = oldFormate.parse(date);

            } catch (ParseException e) {
                AppLog.Log("tag","Exception while formate date"+e);
            }

        if(isOnlyHours){

            SimpleDateFormat newFormate = new SimpleDateFormat("dd/MM/yy,hh a");
            return newFormate.format(myDate);
        }else {
            SimpleDateFormat newFormate = new SimpleDateFormat("dd/MM/yy, hh:mm a");
            return newFormate.format(myDate);
        }

        }

    public static void hideKeybord(Activity activity){

        View view = activity.getCurrentFocus();

        if(view != null){
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showCustomProgressDialog(Context context , boolean isCancelable){

        if(customDialog != null && customDialog.isShowing()){
            return;
        }

        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customDialog.setContentView(R.layout.progress_dialog_view);
        ImageView imgPogrssDialog = (ImageView)customDialog.findViewById(R.id.imgPogrssDialog);
        imgPogrssDialog.setAnimation(AnimationUtils.loadAnimation(context , R.anim.rotation_animation));
        customDialog.setCancelable(isCancelable);
        customDialog.show();

    }

    public static void removeCustomProgressDialog(){
        try{
        if(customDialog != null && customDialog.isShowing()){
            customDialog.dismiss();
            customDialog = null;
        }}catch (Exception e){

        }
    }

    public static String formateDigit(String data){
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        return String.valueOf(decimalFormat.format(Double.parseDouble(data)));
    }

    public static String formatDigitWithPointData(String data){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return String.valueOf(decimalFormat.format(Double.parseDouble(data)));
    }

    public static String getRealPathFromURI(Uri contentURI,Context context){

        String result=null;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null,
                null, null);

        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {
                AppLog.Log("tag","Exception"+e);
            }
            cursor.close();
        }
        return result;
    }

    public static String getPathForBitmap(Bitmap photoBitmap, Context context,int rotationAngle){

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle,
                (float) photoBitmap.getWidth() / 2,
                (float) photoBitmap.getHeight() / 2);
        Bitmap photoBitmapData = Bitmap.createBitmap(photoBitmap, 0, 0,
                photoBitmap.getWidth(),
                photoBitmap.getHeight(), matrix, true);

        return MediaStore.Images.Media.insertImage(
                context.getContentResolver(), photoBitmapData, Calendar
                        .getInstance().getTimeInMillis()
                        + ".jpg", null);

    }

    public static int setRotaionValue(String filePath){

        ExifInterface exif ;
        int rotationAngle = 0;
        try {
            AppLog.Log("tag","File path - "+filePath);
            exif = new ExifInterface(filePath);
            String orientString = exif
                    .getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer
                    .parseInt(orientString)
                    : ExifInterface.ORIENTATION_NORMAL;

            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationAngle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotationAngle = 180;
                    break;
                case  ExifInterface.ORIENTATION_ROTATE_270:
                    rotationAngle = 270;
                    break;
                default:
                    rotationAngle = 0;
                    break;
            }
        } catch (IOException e) {
            AppLog.Log(Const.TAG,Const.EXCEPTION+e);
        }

        return rotationAngle;
    }

 /*   public static String getMapString(String currentLatitude, String currentLongitude, String sourseLatitude,String sourseLongitude){

        return  String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                Float.parseFloat(currentLatitude), Float.parseFloat(currentLongitude),
                "Your Location",
                Float.parseFloat(sourseLatitude), Float.parseFloat(sourseLongitude), "User Location");

    }
*/
    public static void openGoogleMap(Activity activity,String currentLatitude, String currentLongitude, String sourseLatitude,String sourseLongitude) {

        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                Float.parseFloat(currentLatitude), Float.parseFloat(currentLongitude),
                "Your Location",
                Float.parseFloat(sourseLatitude), Float.parseFloat(sourseLongitude), "User Location");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Andyutils.showToast(activity, "Please install a maps application");
            }
        }
    }

    public static void showEmaptyData(int listSize, TextView txtEmpty){
        if (listSize==0) {
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
    }

}
