package com.taxi.passenger.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.taxi.passenger.R;
import com.taxi.passenger.activity.AllTripActivity;

/**
 * Created by Abhilasha Yadav on 9/11/2017.
 */

public class MyFireBaseMessagingService extends FirebaseMessagingService  {
    String msg ;
    SharedPreferences mUserPref,userPref;
    String booking_id;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mUserPref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userPref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        msg= remoteMessage.getNotification().getBody();




        sendNotification(notification, data);



    }/*
    */

    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param notification FCM notification payload received.
     * @param data FCM data payload received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//        SharedPreferences.Editor id = userPref.edit();
//        id.putString("id", userDetilObj.getString("id").toString());
//        id.commit();


   /*     userPref.getString("id","");
        switch(msg){
            case "Your ride is accepted":
            {
                WebService.getInstance().).callAcceptedRideDriverDetail(Integer.parseInt(userPref.getString("id", "")), this);
            }
               break;


            case  "Your ride is cancelled":{

            }
              break;

            case "Your ride is completed":{

            }
            break;

        }*/



/*
        if(msg== "Your ride is accepted") {
            WebService.getInstance().).callAcceptedRideDriverDetail(Integer.parseInt(userPref.getString("id", "")), this);
        }*/
      /*  if (!mUserPref.getBoolean("is_login", false)) {

            Bundle bundle = new Bundle();
            bundle.putString("msg", msg);
            Intent intent = new Intent(this, AllTripActivity.class);
            intent.putExtras(bundle);
           *//* SharedPreferences.Editor message= mUserPref.edit();
            message.putString("msg",msg);*//*
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setContentInfo(notification.getTitle())
                    .setLargeIcon(icon)
                    .setColor(Color.RED)
                    .setSmallIcon(R.mipmap.ic_launcher);

            try {
                String picture_url = data.get("picture_url");
                if (picture_url != null && !"".equals(picture_url)) {
                    URL url = new URL(picture_url);
                    Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    notificationBuilder.setStyle(
                            new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            notificationBuilder.setLights(Color.YELLOW, 1000, 300);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
*/




            Bundle extras = new Bundle();
            extras.putString("msg", msg);
            Intent intent = new Intent(this, AllTripActivity.class);
            intent.putExtras(extras);



            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setContentInfo(notification.getTitle())
                    .setLargeIcon(icon)
                    .setColor(Color.RED)
                    .setSmallIcon(R.mipmap.ic_launcher);

            try {
                String picture_url = data.get("picture_url");
                if (picture_url != null && !"".equals(picture_url)) {
                    URL url = new URL(picture_url);
                    Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    notificationBuilder.setStyle(
                            new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            notificationBuilder.setLights(Color.YELLOW, 1000, 300);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }




/*
    @Override
    public void onSuccess(AccepetedRideDriverDetail accepetedRideDriverDetail) {
        booking_id =accepetedRideDriverDetail.getBookingId();
      Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getBookingId());
        Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getKey());
        Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getKey());
        Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getStatus());
    }

    @Override
    public void onFailure() {

    }*/
}
