package com.example.prueba;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

public class RenewalCheckWorker extends Worker {
    
    private static final String CHANNEL_ID = "subscription_renewals";
    private static final String CHANNEL_NAME = "Renovaciones de Suscripciones";
    private static final int NOTIFICATION_ID = 1001;
    
    public RenewalCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    
    @NonNull
    @Override
    public Result doWork() {
        try {
            // Get database instance
            AppDatabase database = AppDatabase.getInstance(getApplicationContext());
            SubscriptionDao subscriptionDao = database.subscriptionDao();
            
            // Get all subscriptions
            List<Subscription> subscriptions = subscriptionDao.getAllSubscriptionsSync();
            
            // Create notification channel
            createNotificationChannel();
            
            // Check each subscription
            for (Subscription subscription : subscriptions) {
                long daysUntilRenewal = subscription.getDaysUntilRenewal();
                
                // If renewal is within notification days
                if (daysUntilRenewal <= subscription.getNotificationDaysBefore() && daysUntilRenewal >= 0) {
                    sendNotification(subscription);
                }
            }
            
            return Result.success();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notificaciones sobre renovaciones de suscripciones");
            
            NotificationManager notificationManager = getApplicationContext()
                    .getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    private void sendNotification(Subscription subscription) {
        String title = "Renovación Próxima";
        String message = String.format("%s se renovará en %s por %s",
                subscription.getName(),
                subscription.getDaysUntilRenewalText(),
                subscription.getFormattedAmount());
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFICATION_ID + subscription.getId(), builder.build());
    }
}
