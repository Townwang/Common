package com.townwang.update

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.townwang.R
import com.townwang.update.config.CommitUtils
import com.townwang.update.receiver.DownloadBroadcastManager
import com.townwang.update.service.DownloadService
import com.townwang.update.ui.DialogUpdateFragment
import java.io.Serializable


class UpdateHelper @SuppressLint("NewApi")
constructor(versionInfo: VersionInfo) {

    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var mDownloadBroadcastManager: DownloadBroadcastManager
    init {
        versionInfo. newVersion?.run {
          val   status = CommitUtils.VersionComparison(
                versionInfo.newVersion!!,
                versionInfo.newVersion!!,
                CommitUtils.getVersionName(activity!! as Context)!!
            )
            if (status > 0) { //需要更新
                //注册广播接收器
                localBroadcastManager = LocalBroadcastManager.getInstance(activity!!)
                mDownloadBroadcastManager = DownloadBroadcastManager()
                localBroadcastManager.registerReceiver(mDownloadBroadcastManager, IntentFilter("status"))
                localBroadcastManager.registerReceiver(mDownloadBroadcastManager, IntentFilter("rate"))
                @Suppress("DEPRECATION") val ft = activity!!.fragmentManager.beginTransaction()
                ft.add(DialogUpdateFragment.newInstance(versionInfo), "DialogUpdateFragment")
                ft.commit()
            }
        }?.run {
           val  status = CommitUtils.VersionComparison(
                versionInfo.newVersionCode,
                versionInfo.newVersionCode,
                CommitUtils.getAppVersionCode(activity!!)
            )
            if (status > 0) { //需要更新
                //注册广播接收器
                localBroadcastManager = LocalBroadcastManager.getInstance(activity!!)
                mDownloadBroadcastManager = DownloadBroadcastManager()
                localBroadcastManager.registerReceiver(mDownloadBroadcastManager, IntentFilter("status"))
                localBroadcastManager.registerReceiver(mDownloadBroadcastManager, IntentFilter("rate"))
                val ft = activity!!.fragmentManager.beginTransaction()
                ft.add(DialogUpdateFragment.newInstance(versionInfo), "DialogUpdateFragment")
                ft.commit()
            }
        }
    }


    class VersionInfo(activitys: Activity) : Serializable {
        var newVersionCode: Long = -1L // 最新版本
        var newVersion:String? = null // 最新版本
        lateinit var url: String // 下载地址
        var isForce = false
        var titile: String = "检测到有新版本"
        var content: String = "小编也不知道更新了啥！"
        var btnNo: String = "暂不更新"
        var btnOk: String = "立即更新"

        init {
            activity = activitys
        }

        /**
         * 设置最新版本Code
         *
         * @param newVersion 例如520
         * @return
         */
        fun setNewVersionCode(newVersionCode: Long): VersionInfo {
            this.newVersionCode = newVersionCode
            return this
        }

        /**
         * 设置最新版本
         *
         * @param newVersion 例如 1.0.0
         * @return
         */
        fun setNewVersion(newVersion: String): VersionInfo {
            this.newVersion = newVersion
            return this
        }


        /**
         * 设置下载链接
         *
         * @param url
         * @return
         */
        fun setUrl(url: String): VersionInfo {
            this.url = url
            return this
        }

        /**
         * 是否强制更新 默认为【false】
         *
         * @param isForce
         * @return
         */
        fun setForce(isForce: Boolean): VersionInfo {
            this.isForce = isForce
            return this
        }




        /**
         * 设置更新对话框标题 默认为【升级】
         * @param titile
         * @return
         */
        fun setTitile(titile: String): VersionInfo {
            this.titile = titile
            return this
        }

        /**
         * 设置更新内容  默认为 【我也不知道更新了啥】
         * @param content
         * @return
         */
        fun setContent(content: String): VersionInfo {
            this.content = content
            return this
        }

        /**
         * 设置更新对话框取消按钮文案 默认为 【暂不更新】
         * @param btnNo
         * @return
         */
        fun setBtnNo(btnNo: String): VersionInfo {
            this.btnNo = btnNo
            return this
        }

        /**
         * 设置更新对话框更新按钮文案 默认为【立即更新】
         * @param btnOk
         */
        fun setBtnOk(btnOk: String): VersionInfo {
            this.btnOk = btnOk
            return this
        }

        fun build(): UpdateHelper {
            if (this.url == "")
                throw IllegalArgumentException("versionInfo.url == ''")
            if (this.newVersionCode == -1L && this.newVersion ==null )
                throw IllegalArgumentException("versionInfo.newVersionCode == '' or versionInfo.newVersion" )
            return UpdateHelper(this)
        }
    }


    fun clear() {
        activity = null
        //取消注册广播,防止内存泄漏
        localBroadcastManager.unregisterReceiver(mDownloadBroadcastManager)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
         var activity: Activity? = null

        /**
         * 下载成功
         */
        val DOWNLOADSERVICE_STATUS_OK = 1

        /**
         * 下载失败
         */
        val DOWNLOADSERVICE_STATUS_NO = 2

        /**
         * 启动服务，开始下载
         */
        fun startUpdate(mVersionInfo: VersionInfo) {
            val intent = Intent(activity, DownloadService::class.java)
            intent.putExtra("mVersionInfo", mVersionInfo)
            activity!!.startService(intent)
        }
    }

}
