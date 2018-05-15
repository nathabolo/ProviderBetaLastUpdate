package com.tradesman.provider;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.tradesman.provider.utils.AppLog;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.utils.Andyutils;
import com.tradesman.provider.utils.Const;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Elluminati Mohit on 1/13/2017.
 */

public class SpleshScreenActivity extends ActionBarBaseActivity implements View.OnClickListener {

    private Button btnSignIn,btnRegister;
    private Timer checkStatus;
    private int timerSchedule = 4*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        startService(new Intent(this, RealTimeService.class));
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle("JimmieJobs").setContentText("Welcome").
                setContentTitle("JimmieJobs Provider").setSmallIcon(R.drawable.app_icon).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);

        btnSignIn = (Button) findViewById(R.id.btnSplashSignIn);
        btnRegister = (Button) findViewById(R.id.btnSplashRegister);
        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnSplashSignIn:
                Intent signIntent = new Intent(this,SigninActivity.class);
                startActivity(signIntent);
                break;
            case R.id.btnSplashRegister:
                Intent regIntent = new Intent(this,RegisterActivity.class);
                startActivity(regIntent);
                break;
            default:
                AppLog.Log(Const.TAG,"Default case");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetworkStatus();
        stopCheckStatus();
        if(!Andyutils.isNetworkAvailable(this)){
            startCheckStatus();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopCheckStatus();
    }

    public void checkNetworkStatus(){

        if(Andyutils.isNetworkAvailable(this) && !TextUtils.isEmpty(preferenceHelper.getUSERID())){
            stopCheckStatus();
            AppLog.Log("tag","data - selected- "+preferenceHelper.getisProviderTypeSelected());
            if(preferenceHelper.getisProviderTypeSelected()){
                startActivity(new Intent(this,AvailableJobActivity.class));
            }else {
                startActivity(new Intent(this,AvailableJobActivity.class));
            }

            finishAffinity();
        }
        else if(Andyutils.isNetworkAvailable(this)){
            stopCheckStatus();
            closeNetworkDialog();
        }else {
            openNetworkDialog();

        }
    }


    public class TrackNetworlStatus extends TimerTask{

        @Override
        public void run() {
           checkNetworkStatus();
        }
    }

    public void startCheckStatus(){
        checkStatus = new Timer();
        checkStatus.scheduleAtFixedRate(new TrackNetworlStatus(),0, timerSchedule);
    }

    public void stopCheckStatus(){

        if(checkStatus != null){
            checkStatus.cancel();
            checkStatus = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCheckStatus();
    }
}
