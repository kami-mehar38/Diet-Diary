package a098.ramzan.kamran.paleodietdiary;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        long[] pattern = new long[]{500, 1000, 500};
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(SettingsNotifications.ALARM_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Paleo Diet Diary")
                .setContentText("You forget to add your diet plan in Paleo Diet Diary").setVibrate(pattern)
                .setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(167, mBuilder.build());
    }
}
