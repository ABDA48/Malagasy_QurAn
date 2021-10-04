package google.abdallah.quranproject.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import java.io.File;

import google.abdallah.quranproject.R;

import static google.abdallah.quranproject.Prayer.AppController.Channel1;

public class Azan extends BroadcastReceiver {
    NotificationManagerCompat notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationAlarm(context);
        MediaPlayer player=MediaPlayer.create(context,R.raw.azan);
        player.start();

    }
    void NotificationAlarm(Context context){
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notificationManager= NotificationManagerCompat.from(context);
         Notification notification=new NotificationCompat.Builder(context,Channel1)
             .setSmallIcon(R.drawable.qibla)
             .setContentText("Swalat time")
             .setContentTitle("Azan")
             .setCategory(Notification.CATEGORY_ALARM)
             .build();
        notificationManager.notify(1,notification);



    }
}
