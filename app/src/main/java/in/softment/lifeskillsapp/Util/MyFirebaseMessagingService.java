package in.softment.lifeskillsapp.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import in.softment.lifeskillsapp.R;
import in.softment.lifeskillsapp.WelcomeActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String Channel_Name = "ch_lsa";
    String Channel_Id = "cid_lsa";
    NotificationChannel notificationChannel;
    int rq = 10;
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        String title = "";
        String body = "";
try {
    title = remoteMessage.getNotification().getTitle();
    body = remoteMessage.getNotification().getBody();
}
catch (Exception e) {

    title = remoteMessage.getData().get("title");
    body = remoteMessage.getData().get("message");

}


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,Channel_Id)
                .setContentTitle(title).setAutoCancel(true).setContentText(body).setStyle(new NotificationCompat.BigTextStyle().bigText(body));


        mBuilder.setSmallIcon(R.drawable.ic_baseline_notifications_active_24);
        mBuilder.setColor(getResources().getColor(R.color.main_color));
         mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));//Here is FILE_NAME is the name of file that you want to play


        // Vibrate if vibrate is enabled
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);


        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent notificationIntent = new Intent(this, WelcomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(notificationIntent);
        // set intent so it does not start a new activity
       PendingIntent intent =  taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(intent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            NotificationChannel mChannel = new NotificationChannel(Channel_Id,
                      getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            mChannel.setDescription(body);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);



            if (notificationManager != null)
               notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(new Random().nextInt(), mBuilder.build());



    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);


    }



}
