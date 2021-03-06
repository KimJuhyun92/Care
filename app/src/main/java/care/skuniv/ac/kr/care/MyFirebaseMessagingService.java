package care.skuniv.ac.kr.care;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by 김주현 on 2017-11-16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("checkmsg", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            String msg = remoteMessage.getData().get("message");
            try {
                msg = URLDecoder.decode(msg, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("<<<<<<<<msgmsg>>>>>>>", msg);
            sendNotification(msg);
        }
        if (remoteMessage.getNotification() != null) {
            String msg = remoteMessage.getNotification().getBody();
            try {
                msg = URLDecoder.decode(msg, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sendNotification(msg);
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Metor Academy")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}


