package com.example.chat;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
String TAG="MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        sendNotification(remoteMessage);


        Log.e(TAG, "onMessageReceived: "+remoteMessage.getNotification().getBody() );

    }

    public void sendNotification(final RemoteMessage remoteMessage){

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"ch");
        builder.setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);





    }

}
