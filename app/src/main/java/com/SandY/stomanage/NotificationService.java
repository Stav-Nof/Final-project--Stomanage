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
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify((int)(Math.random() * 100), builder.build());
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
        notificationManager.notify((int)(Math.random() * 100), builder.build());
    }


    public static void sendNotification(String body, String title) {
//        // This registration token comes from the client FCM SDKs.
//        String registrationToken = "fumkxxbJSDqJptB8mXjm5m:APA91bGNEGRM2YJGspB8WiQSqXKlujnbhtzEWYG43pwKaMwUdv69h7-0cxaTR0oEEheYmgiEYjYg4AJOMKFgXdxtvNpLcdQr8QC0nyz-FT-hYsCNeeMT0x1A9eXqMZEXmlUtsLm8Y3Gq";
//
//        // See documentation on defining a message payload.
//        Message message = Message.builder()
//                .putData("title", "or")
//                .putData("text", "orOr")
//                .setToken(registrationToken)
//                .build();
//
//// Send a message to the device corresponding to the provided
//// registration token.
//        String response = FirebaseMessaging.getInstance().send(message);
//// Response is a message ID string.
//        System.out.println("Successfully sent message: " + response);
    }

}
