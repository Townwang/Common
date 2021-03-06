package com.townwang.keepalive.receiver

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.townwang.keepalive.ui.OnePixelActivity

class OnepxReceiver : BroadcastReceiver() {

    private var appIsForeground = false

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF) {    //屏幕关闭的时候接受到广播
            appIsForeground = isForeground(context)
            try {
                val it = Intent(context, OnePixelActivity::class.java)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (intent.action == Intent.ACTION_SCREEN_ON) {   //屏幕打开的时候发送广播  结束一像素
            context.sendBroadcast(Intent("finish activity"))
            if (!appIsForeground) {
                appIsForeground = false
                try {
                    val home = Intent(Intent.ACTION_MAIN)
                    home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    home.addCategory(Intent.CATEGORY_HOME)
                    context.applicationContext.startActivity(home)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

   private fun isForeground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION") val tasks = am.getRunningTasks(1)
        if (tasks != null && tasks.isNotEmpty()) {
            val topActivity = tasks[0].topActivity
            if (topActivity.packageName == context.packageName) {
                return true
            }
        }
        return false
    }

}