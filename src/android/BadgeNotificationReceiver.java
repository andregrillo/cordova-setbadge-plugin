// BadgeNotificationReceiver.java

package com.outsystems.setbadge.plugin; 

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class BadgeNotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "badge_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String isIncrementBadge = intent.getStringExtra("isIncrementBadge");
        String badgeCountString = intent.getStringExtra("setBadge");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Badge Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Badge Update")
                .setContentText("Updating badge...")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setDefaults(NotificationCompat.DEFAULT_NONE);

        if ("true".equals(isIncrementBadge)) {
            builder.setNumber(notificationManager.getActiveNotifications().length + 1);
        } else if (badgeCountString != null) {
            builder.setNumber(Integer.parseInt(badgeCountString));
        }

        notificationManager.notify(1, builder.build());
    }
}