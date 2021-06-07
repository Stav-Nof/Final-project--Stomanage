package com.SandY.stomanage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class NotificationService {

    public static final String orders = "Orders";
    public static final String factories = "Factories";

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name1 = context.getString(R.string.orders);
            String description1 = context.getString(R.string.orders);
            int importance1 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel1 = new NotificationChannel(orders, name1, importance1);
            channel1.setDescription(description1);

            CharSequence name2 = context.getString(R.string.factories);
            String description2 = context.getString(R.string.factories);
            int importance2 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel2 = new NotificationChannel(factories, name2, importance2);
            channel2.setDescription(description2);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }

    public static void NotificationOrders(Context context, String title, String text) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, orders)
                .setSmallIcon(R.drawable.image_not_available)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public static void NotificationFactories(Context context, String title, String text) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, factories)
                .setSmallIcon(R.drawable.image_not_available)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(2, builder.build());
    }

}
