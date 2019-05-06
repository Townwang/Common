package com.townwang.keepalive.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.townwang.keepalive.KeepLive
import com.townwang.keepalive.config.NotificationUtils
import com.townwang.keepalive.receiver.NotificationClickReceiver

class HideForegroundService : Service() {

    private var handler: android.os.Handler? = null
    @SuppressLint("NewApi")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground()
        if (handler == null) {
            handler = Handler()
        }
        handler!!.postDelayed({
            stopForeground(true)
            stopSelf()
        }, 2000)
        return Service.START_NOT_STICKY
    }


    @SuppressLint("NewApi")
    private fun startForeground() {
        if (KeepLive.foregroundNotification != null) {
            val intent = Intent(applicationContext, NotificationClickReceiver::class.java)
            intent.action = NotificationClickReceiver.CLICK_NOTIFICATION
            val notification = NotificationUtils.createNotification(this, KeepLive.foregroundNotification!!.getTitle(), KeepLive.foregroundNotification!!.getDescription(), KeepLive.foregroundNotification!!.getIconRes(), intent)
            startForeground(13691, notification)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}