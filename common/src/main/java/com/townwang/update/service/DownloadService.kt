package com.townwang.update.service

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.townwang.update.UpdateHelper

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * 开启子线程的服务下载APK，下载完毕后通过广播通知安装替换
 */
@SuppressLint("NewApi")
class DownloadService : IntentService("DownloadService") {
    private lateinit var mVersionInfo: UpdateHelper.VersionInfo
    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private val saveDir: File by lazy {
        File(getExternalFilesDir(Environment.DIRECTORY_MOVIES)!!.toString() + "/apk/")
    }
    private val saveFile: File by lazy {
        File(saveDir, "update.apk")
    }

    private val httpConnection: HttpURLConnection by lazy {
        URL(mVersionInfo.url).openConnection() as HttpURLConnection
    }

    override fun onHandleIntent(intent: Intent?) {
        mVersionInfo = intent!!.getSerializableExtra("mVersionInfo") as UpdateHelper.VersionInfo

        try {
            download()
        } catch (e: Exception) {
            e.printStackTrace()
            val intent1 = Intent("status")
            intent1.putExtra("status", UpdateHelper.DOWNLOADSERVICE_STATUS_NO)
            intent.putExtra("saveFile", saveFile.path)
            localBroadcastManager.sendBroadcast(intent1)
        }

    }

    /**
     * 开始下载
     */
    @SuppressLint("NewApi")
    @Throws(Exception::class)
    private fun download() {
        Log.d("APP更新:", "开始")
        saveDir.run {
            mkdirs()
        }
        saveFile.run {
            createNewFile()
        }
        val mInputStream: InputStream by lazy {
            httpConnection.inputStream
        }
        val mFileOutputStream: FileOutputStream by lazy {
            FileOutputStream(saveFile, false)
        }
        try {
            httpConnection.apply {
                setRequestProperty("User-Agent", "PacificHttpClient")
                setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                connectTimeout = 200000
                readTimeout = 200000
                if (responseCode == 404) { //下载失败
                    throw Exception("404")
                }

            }
            someFunc(mInputStream, mFileOutputStream)
        } finally {
            httpConnection.disconnect()
            mInputStream.close()
            mFileOutputStream.close()
        }

    }

    private fun someFunc(`in`: InputStream, output: OutputStream) {
        try {
            var read: Int
            var downloadCount = 0
            var totalSize = 0.0
            val buffer = ByteArray(1024 * 1024 * 3)
            `in`.use { input ->
                output.use {
                    while (input.read(buffer).also {
                            read = it
                        } != -1) {
                        it.write(buffer, 0, read)
                        totalSize += read.toLong()
                        val a = ((totalSize*1.0f)  / httpConnection.contentLength)*100
                        if (downloadCount == 0 || a > downloadCount) {
                            downloadCount++
                            val intent = Intent("rate")
                            intent.putExtra("rate", downloadCount)
                            localBroadcastManager.sendBroadcast(intent)
                        }
                    }
                }
            }
            val intent = Intent("status")
            intent.putExtra("status", UpdateHelper.DOWNLOADSERVICE_STATUS_OK)
            intent.putExtra("saveFile", saveFile.path)
            localBroadcastManager.sendBroadcast(intent)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
