package com.townwang.keepalive.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.os.RemoteException
import com.townwang.keepalive.config.NotificationUtils
import com.townwang.keepalive.receiver.NotificationClickReceiver
import com.townwang.keeplive.GuardAidl

class RemoteService : Service() {

    private var mBilder: MyBilder? = null

    override fun onCreate() {
        super.onCreate()
        if (mBilder == null) {
            mBilder = MyBilder()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBilder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            this.bindService(Intent(this@RemoteService, LocalService::class.java),
                    connection, Context.BIND_ABOVE_CLIENT)
        } catch (e: Exception) {
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    private inner class MyBilder : GuardAidl.Stub(), IBinder {
        @SuppressLint("NewApi")
        @Throws(RemoteException::class)
        override fun wakeUp(title: String, discription: String, iconRes: Int) {
            if (Build.VERSION.SDK_INT < 25) {
                val intent = Intent(applicationContext, NotificationClickReceiver::class.java)
                intent.action = NotificationClickReceiver.CLICK_NOTIFICATION
                val notification = NotificationUtils.createNotification(this@RemoteService, title, discription, iconRes, intent)
                this@RemoteService.startForeground(13691, notification)
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            val remoteService = Intent(this@RemoteService,
                    LocalService::class.java)
            this@RemoteService.startService(remoteService)
            this@RemoteService.bindService(Intent(this@RemoteService,
                    LocalService::class.java), this, Context.BIND_ABOVE_CLIENT)
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {}
    }

}