package com.tradesman.provider.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;

import com.tradesman.provider.MyJobActivity;
import com.tradesman.provider.utils.AppLog;
import com.tradesman.provider.AvailableJobActivity;
import com.jimmiejobscreative.tradesman.provider.R;;
import com.tradesman.provider.utils.Const;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Elluminati Mohit on 1/19/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Intent notificationIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String message = remoteMessage.getData().get("message");
        String displyMessage = remoteMessage.getData().get("message_title");
        Intent pushIntent = new Intent(Const.ProviderPushStatus.PROVIDER_STATUS);
        pushIntent.putExtra(Const.ProviderPushStatus.PROVIDER_APPROVE_STATUS,message);

        generateNotificationNew(this,displyMessage,message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushIntent);


    }


    private void generateNotificationNew(Context context, String message,String pushIdmessage) {

        Intent resultIntent = null;
        int count;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setContentText(message);

        int pushId = getPushId(pushIdmessage);
        switch (pushId){

            case 1:
            case 2:
                count = Const.PushNotificationCount.APPROVE_OR_DECLINE;
                resultIntent = new Intent(context,AvailableJobActivity.class);
                break;
            case 3:
                count = Const.PushNotificationCount.NEW_REQUEST;
                resultIntent = new Intent(context,AvailableJobActivity.class);
                break;
            case 5:
                count = Const.PushNotificationCount.DELETE_QUOTE;
                resultIntent = new Intent(context,AvailableJobActivity.class);
                break;
            case 4:
                count = Const.PushNotificationCount.ACCEPT_COST;
                resultIntent = new Intent(context,MyJobActivity.class);
                break;
            case 6:
                count = Const.PushNotificationCount.CANCEL_JOB;
                resultIntent = new Intent(context,MyJobActivity.class);
                break;

            default:
                count = Const.PushNotificationCount.NEW_REQUEST;
                resultIntent = new Intent(context,AvailableJobActivity.class);

        }


        //Intent resultIntent = new Intent(this, AvailableJobActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AvailableJobActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        5,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(count, mBuilder.build());



    }

    public int getPushId(String message){
        try {
            JSONObject jsonObject = new JSONObject(message);
            return  jsonObject.getInt("push_id");
        } catch (JSONException e) {
            AppLog.Log(Const.TAG,Const.EXCEPTION+e);
        }
        return -1;
    }
}
