package com.townwang.update.config;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class UpdateNotificationUtils {
    // 通知栏
    private static NotificationManager updateNotificationManager = null;
    private static Notification.Builder updateNotification = null;
    // 通知栏跳转Intent
    private static Intent updateIntent = null;
    private static PendingIntent updatePendingIntent = null;
    public static NotificationManager getNotificationManager(Context mContext) {
        if (updateNotificationManager == null) {
            updateNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return updateNotificationManager;
    }


    @SuppressLint("NewApi")
    public static Notification.Builder getNotificationBuilder(Context mContext) {

        if (updateNotification == null) {
            updateNotification = new Notification.Builder(mContext);
        }
        return updateNotification;
    }

    public static Intent getIntent() {
        if (updateIntent == null) {
            updateIntent = new Intent();
        }
        return updateIntent;
    }

    public static PendingIntent getPendingIntent(Context mContext) {
        if (updatePendingIntent == null) {
            updatePendingIntent = PendingIntent.getActivity(mContext, 0, updateIntent == null ? getIntent() : updateIntent, 0);
        }
        return updatePendingIntent;
    }


}
