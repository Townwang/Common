package com.townwang.update.receiver

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.townwang.BuildConfig
import com.townwang.R
import com.townwang.update.UpdateHelper
import com.townwang.update.config.NotificationUtils

import java.io.File


class DownloadBroadcastManager : BroadcastReceiver() {
    private var mContext: Context? = null
    private var saveFile: File? = null


    // 通知栏跳转Intent
    private var updateIntent: Intent? = null
    private var updatePendingIntent: PendingIntent? = null
    val mIntent: Intent?
        get() = updateIntent ?: Intent()

    override fun onReceive(context: Context, intent: Intent) {
        mContext = context
        when(intent.action){
            "rate" ->{
                val reat = intent.getIntExtra("rate", 0)
                setNotification(reat)
            }
            "status"->{
                saveFile = File(if (intent.getStringExtra("saveFile") == null) "" else intent.getStringExtra("saveFile"))
                val status = intent.getIntExtra("status", UpdateHelper.DOWNLOADSERVICE_STATUS_NO)
                checkoutStatus(status)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun checkoutStatus(status: Int) {
        if (status == UpdateHelper.DOWNLOADSERVICE_STATUS_OK) {
            val install = Intent(Intent.ACTION_VIEW)
            if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
                val apkUri = FileProvider.getUriForFile(
                    mContext!!,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    saveFile!!
                )//在AndroidManifest中的android:authorities值
                install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//添加这一句表示对目标应用临时授权该Uri所代表的文件
                install.setDataAndType(apkUri, "application/vnd.android.package-archive")
                mContext!!.startActivity(install)
            } else {
                install.setDataAndType(Uri.fromFile(saveFile), "application/vnd.android.package-archive")
                install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                mContext!!.startActivity(install)
            }
            // 当下载完毕，更新通知栏，且当点击通知栏时，安装APK
            val installIntent = PendingIntent.getActivity(mContext, 0, install, 0)
            val notificationUtils = NotificationUtils(mContext!!)
            notificationUtils.sendNotification("升级", "下载完成，点击安装", installIntent)

        } else {
            val notificationUtils = NotificationUtils(mContext!!)
            notificationUtils.sendNotification("升级失败", "网络出现异常", updatePendingIntent ?: PendingIntent.getActivity(mContext, 0, mIntent ?: updateIntent, 0))
        }

    }


    @SuppressLint("NewApi")
    private fun setNotification(rate: Int) {
        val intent = Intent("dialog_te")
        intent.putExtra("dialog_te", rate)
        mContext?.sendBroadcast(intent)
        val notificationUtils = NotificationUtils(mContext!!)
        notificationUtils.sendNotificationProgress(
            mContext!!.resources.getString(R.string.app_name) + " 升级"
            , "$rate%", rate, updatePendingIntent ?: PendingIntent.getActivity(mContext, 0, mIntent ?: updateIntent, 0)
        )

    }
}