// BadgeNotificationReceiver.java

package outsystems.setbadge.plugin; // Replace this with the actual package name for your project

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
        String shouldIncrementBadge = intent.getStringExtra("shouldIncrementBadge");
        String badgeCountString = intent.getStringExtra("badge");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Badge Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Badge Update")
            .setContentText("Updating badge...")
            .setSmallIcon(R.drawable.ic_notification)  // Replace with a valid icon resource
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

        if ("true".equals(shouldIncrementBadge)) {
            builder.setNumber(notificationManager.getActiveNotifications().length + 1);  // Increment badge count
        } else if (badgeCountString != null) {
            builder.setNumber(Integer.parseInt(badgeCountString));  // Set badge count directly
        }

        notificationManager.notify(1, builder.build());
    }
}
