
package com.townwang.update.config


import android.annotation.TargetApi
import android.app.Notification.VISIBILITY_SECRET
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT


/**
 * @Remarks 通知栏处理工具
 */
class NotificationUtils(base: Context) : ContextWrapper(base) {
    private val manager: NotificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

        channel.run {
            //是否绕过请勿打扰模式
            canBypassDnd()
            //闪光灯
            enableLights(true)
            //锁屏显示通知
            lockscreenVisibility = VISIBILITY_SECRET
            //闪关灯的灯光颜色
            lightColor = Color.RED
            //桌面launcher的消息角标
            canShowBadge()
            //是否允许震动
            enableVibration(true)
            //获取系统通知响铃声音的配置
            audioAttributes
            //获取通知取到组
            group
            //设置可绕过 请勿打扰模式
            setBypassDnd(true)
            //设置震动模式
            vibrationPattern = longArrayOf(100, 100, 200)
            //是否会有灯光
            shouldShowLights()
        }
        manager.createNotificationChannel(channel)
    }

    /**
     * 发送通知
     */
    fun sendNotification(title: String, content: String,contentIntent:PendingIntent) {
        val builder = getNotification(title, content,contentIntent)
        manager.notify(0, builder.build())
    }

    private fun getNotification(title: String, content: String,contentIntent:PendingIntent): NotificationCompat.Builder {
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            builder = NotificationCompat.Builder(applicationContext)
            builder.priority = PRIORITY_DEFAULT
        }
        builder.run {
            setContentIntent(contentIntent)
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(android.R.drawable.stat_sys_download)
            //设置点击信息后自动清除通知
            setAutoCancel(true)
        }
        return builder
    }

    /**
     * 发送通知
     */
    fun sendNotification(notifyId: Int, title: String, content: String,contentIntent:PendingIntent) {
        val builder = getNotification(title, content,contentIntent)
        manager.notify(notifyId, builder.build())
    }

    /**
     * 发送带有进度的通知
     */
    fun sendNotificationProgress(title: String, content: String, progress: Int, intent: PendingIntent) {
        val builder = getNotificationProgress(title, content, progress, intent)
        manager.notify(0, builder.build())
    }

    /**
     * 获取带有进度的Notification
     */
    private fun getNotificationProgress(title: String, content: String, progress: Int, intent: PendingIntent): NotificationCompat.Builder {
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            builder = NotificationCompat.Builder(applicationContext)
            builder.priority = PRIORITY_DEFAULT
        }

        builder.run {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(android.R.drawable.stat_sys_download)
            //设置点击信息后自动清除通知
            setAutoCancel(true)
            setProgress(100, progress, false)
            //设置点击信息后自动清除通知
            setAutoCancel(true)
            //通知的时间
            setWhen(System.currentTimeMillis())
            //设置点击信息后的跳转（意图）
            setContentIntent(intent)
        }

        return builder
    }

    companion object {
        const val CHANNEL_ID = "default"
        private const val CHANNEL_NAME = "Default Channel"
    }
}

