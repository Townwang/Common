package com.townwang

import android.app.Application
import android.content.Context
import android.content.Intent
import com.townwang.keepalive.KeepLive
import com.townwang.keepalive.config.ForegroundNotification
import com.townwang.keepalive.config.ForegroundNotificationClickListener
import com.townwang.keepalive.config.KeepLiveService


class App :  Application(){

    override fun onCreate() {
        super.onCreate()
//        进程保活
        KeepLive.startWork(
            this,
            KeepLive.RunMode.ROGUE,
            ForegroundNotification("进程保活", "正在运行...",
            R.mipmap.ic_launcher, object : ForegroundNotificationClickListener {
                override fun foregroundNotificationClick(context: Context, intent: Intent) {
                    //点击通知回调
                }
            }),
            object : KeepLiveService {
            override fun onStop() {
                //可能调用多次，跟onWorking匹配调用
            }
            override fun onWorking() {
                //一直存活，可能调用多次
            }
        })





    }
}