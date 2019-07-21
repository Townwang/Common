package com.townwang.update.config

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log


object CommitUtils {

    /**
     * 版本更新比较三个版本（配置时要注意但是最新版本不能小于最小版本）
     *  // 状态   1：必须更新  2：可以不更新  ;0：不要更新
     */
    fun VersionComparison(current_version: Long, min_version: Long, my_version: Long): Int {
        var staet = 0
        if (my_version == current_version) {
            staet = 0
        } else if (current_version > my_version) {
            staet = if (my_version > min_version) {
                2
            } else {
                1
            }
        }
        return staet
    }


    /**
     * 版本更新比较三个版本（配置时要注意但是最新版本不能小于最小版本）
     *
     * @param current_version 最新版本
     * @param min_version     最小版本
     * @return
     */
    fun VersionComparison(current_version: String, min_version: String, my_version: String): Int {

        //        状态   1：必须更新  2：可以不更新  ;0：不要更新
        var staet = 0

        //        最新版本
        val currentVersion = current_version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        //        最低要求版本
        val minVersion = min_version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        //        当前版本
        val myVersion = my_version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val size_1 = Math.max(myVersion.size, minVersion.size)
        val size = Math.max(currentVersion.size, size_1)
        for (i in 0 until size) {
            staet = VersionComparison(i, currentVersion, minVersion, myVersion)
            if (staet != -1) {
                break
            }
        }
        if (staet == -1) {
            staet = 0
        }

        return staet
    }



    /**
     * 辅助VersionComparison（）
     *
     * @param i
     * @param currentVersion
     * @param minVersion
     * @param myVersion
     * @return
     */
    private fun VersionComparison(
        i: Int,
        currentVersion: Array<String>,
        minVersion: Array<String>,
        myVersion: Array<String>
    ): Int {
        // 状态   1：必须更新  2：可以不更新  ;0：不要更新
        var staet = -1
        val intCurrentVersion =  if (currentVersion.size > i) {
            Integer.valueOf(currentVersion[i])
        } else {
            0
        }
        val intMinVersion =   if (minVersion.size > i) {
             Integer.valueOf(minVersion[i])
        } else {
             0
        }
       val intVersion = if (myVersion.size > i) {
            Integer.valueOf(myVersion[i])
        } else {
            0
        }
        if (intVersion < intCurrentVersion) {
            staet = 2
            if (intVersion < intMinVersion) {
                staet = 1
            }
            return staet
        }
        if (intVersion < intMinVersion && intVersion < intCurrentVersion) {
            staet = 1
        } else if (intVersion in intMinVersion until intCurrentVersion) {
            staet = 2
        } else if (intVersion > intMinVersion && intVersion > intCurrentVersion) {
            staet = 0
        } else if (intVersion == intMinVersion && intVersion == intCurrentVersion) {
            staet = -1
        }


        return staet
    }

    /**
     * 获取应用程序版本名称信息
     */
    fun getVersionName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            return packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
    /**
     * 获取当前app version code
     */
    fun getAppVersionCode(context: Context): Long {
        var appVersionCode: Long = 0
        try {
            val packageInfo = context.applicationContext
                .packageManager
                .getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                appVersionCode = packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("获取信息", e.message)
        }

        return appVersionCode
    }
    /**
     * 屏幕宽
     *
     * @param context
     * @return
     */
    fun getWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * 屏幕高
     *
     * @param context
     * @return
     */
    fun getHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

}
