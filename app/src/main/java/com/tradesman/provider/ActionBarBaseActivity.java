package com.tradesman.provider;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tradesman.provider.parse.ParseContent;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.utils.Const;
import com.jimmiejobscreative.tradesman.provider.R;
import com.tradesman.provider.dialog.CustomDialogNoEdt;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.PreferenceHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class ActionBarBaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    protected Toolbar toolbar;
    protected TextView tvActionBarTitle;
    protected ImageButton btnActionBarBack;
    protected ImageButton btnDrawerToggle;
    private GoogleApiClient googleApiClient;


    protected PreferenceHelper preferenceHelper;
    protected ParseContent parseContent;
    private CustomDialogNoEdt gpsDialog;
    private CustomDialogNoEdt networkDialog;
    private CustomDialogNoEdt locationPerDialog;
    private CustomDialogNoEdt locationNeverAskAgainDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelper = PreferenceHelper.getInstance(this);
        parseContent = new ParseContent(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Const.NETWORK_CHANGE);
        intentFilter.addAction(Const.GPS_CHANGE);
        registerReceiver(changeReceiver, intentFilter);
    }

    protected BroadcastReceiver changeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String getIntentData = intent.getAction();
            if (TextUtils.equals(getIntentData, Const.NETWORK_CHANGE)) {
                onBrodcastNetworkChange(context);
            }
            if (TextUtils.equals(getIntentData, Const.GPS_CHANGE)) {
                onBrodcastGpsChange(context);
            }
        }
    };

    protected void onBrodcastNetworkChange(Context context) {
        if (Andyutils.isNetworkAvailable(context)) {
            closeNetworkDialog();
        } else {
            openNetworkDialog();
        }
    }

    protected void onBrodcastGpsChange(Context context) {
        if (Andyutils.isGPSAvailable(context)) {
            closeGpsDialog();
        } else {
            openGpsDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            buildGoogleApiClient();
            googleApiClient.connect();
        }

        if (Andyutils.isGPSAvailable(this)) {
            closeGpsDialog();
        } else {
            openGpsDialog();
        }
        if (Andyutils.isNetworkAvailable(this)) {
            closeNetworkDialog();
        } else {
            openNetworkDialog();
        }


    }

    public void openGpsDialog() {

        if (gpsDialog != null && gpsDialog.isShowing()) {
            return;
        }

        gpsDialog = new CustomDialogNoEdt(this, null, getString(R.string.gps_dialog_message), getString(R.string.OK),
                getString(R.string.CANCEL), false) {
            @Override
            public void btnOkClickListner() {
                startActivityForResult(new Intent(android.provider.Settings
                        .ACTION_LOCATION_SOURCE_SETTINGS), Const.ACTION_LOCATION_SETTINGS);
            }

            @Override
            public void btnCancelClickListner() {
                dismiss();
                finishAffinity();
            }
        };
        gpsDialog.show();

    }

    public void closeGpsDialog() {

        if (gpsDialog != null && gpsDialog.isShowing()) {
            gpsDialog.dismiss();
            gpsDialog = null;
        }
    }

    public void openNetworkDialog() {

        if (networkDialog != null && networkDialog.isShowing()) {
            return;
        }

        networkDialog = new CustomDialogNoEdt(this, null, getString(R.string.internet_dialog_message), getString(R.string.OK),
                getString(R.string.CANCEL), false) {

            @Override
            public void btnOkClickListner() {
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), Const.ACTION_SETTING);

            }

            @Override
            public void btnCancelClickListner() {
                closeNetworkDialog();
                finishAffinity();
            }
        };


        networkDialog.show();

    }

    public void closeNetworkDialog() {

        if (networkDialog != null && networkDialog.isShowing()) {
            networkDialog.dismiss();
            networkDialog = null;
        }
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        tvActionBarTitle = (TextView) toolbar.findViewById(R.id.tvActionBarTitle);
        btnActionBarBack = (ImageButton) toolbar.findViewById(R.id.btnActionBarBack);
        btnDrawerToggle = (ImageButton) toolbar.findViewById(R.id.btnDrawerToggle);
        btnActionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(changeReceiver);
    }


    public void setToolBarTitle(String title) {
        //tvActionBarTitle.setText(title);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(10000);
        mlocationRequest.setFastestInterval(10000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mlocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        AppLog.Log("tag","Connection Suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        preferenceHelper.putLatitude(String.valueOf(latitude));
        preferenceHelper.putLongitude(String.valueOf(longitude));

    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Const.ACTION_SETTING){
            onresultActionSetting();
        }else if (resultCode == Const.ACTION_LOCATION_SETTINGS){
            onresultActionLocationSetting();
        }

    }

    public void onresultActionSetting(){
        closeNeveraskDialog();


        if(Andyutils.isNetworkAvailable(this)){
            closeNetworkDialog();
        }else {
            openNetworkDialog();
        }
    }

    public void onresultActionLocationSetting(){
        if(Andyutils.isGPSAvailable(this)){
            closeGpsDialog();
        }else {
            openGpsDialog();
        }
    }


    protected void checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                    .ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, Const.PERMISSION_LOCATION_REQUEST_CODE);
        }
    }


    protected void openLocationDialog(Activity activity){

        if(locationPerDialog != null && locationPerDialog.isShowing()){
            return;
        }
        locationPerDialog = new CustomDialogNoEdt(activity,null,getString(R.string.location_dialog_message),
                getString(R.string.OK),getString(R.string.location_dialog_exit),false) {
            @Override
            public void btnOkClickListner() {
                dismiss();
                checkLocationPermission();
            }

            @Override
            public void btnCancelClickListner() {
                dismiss();
                finishAffinity();
            }
        };
        locationPerDialog.show();
    }

    public void openNeverAskDialog(){

        locationNeverAskAgainDialog = new CustomDialogNoEdt(this,null,getString(R.string.location_neverask_message),
                getString(R.string.OK),getString(R.string.CANCEL),false) {
            @Override
            public void btnOkClickListner() {
                dismiss();
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),Const.ACTION_SETTING);
            }

            @Override
            public void btnCancelClickListner() {
                dismiss();
                finishAffinity();
            }
        };
        locationNeverAskAgainDialog.show();
    }

    public void closeLocationDialog(){
        if(locationPerDialog != null && locationPerDialog.isShowing()){
            locationPerDialog.dismiss();
            locationPerDialog = null;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Const.PERMISSION_LOCATION_REQUEST_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                AppLog.Log("tag","Permission Granted");
            }else if(ActivityCompat.shouldShowRequestPermissionRationale(this , permissions[0])){
                openLocationDialog(this);
            }
            else {
                openNeverAskDialog();
            }
        }
    }


    public void closeNeveraskDialog(){

        if(locationNeverAskAgainDialog != null && locationNeverAskAgainDialog.isShowing()){
            locationNeverAskAgainDialog.dismiss();
            locationNeverAskAgainDialog = null;
        }
    }


    public void goToAvailabeleJob(Activity activity){
        Intent intent = new Intent(activity,AvailableJobActivity.class);
        activity.startActivity(intent);
    }
}
