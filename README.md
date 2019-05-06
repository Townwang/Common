# Common
[![Download](https://api.bintray.com/packages/townwang/Common/common/images/download.svg) ](https://bintray.com/townwang/Common/common/_latestVersion)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

>>安卓中通用的工具库，自用

## 一句话引入

```
api 'com.townwang:common:1.0.0'
```
## 功能

### 进程保活

#### 使用方法

1. 加入权限

``` <!--权限配置-->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <uses-permission android:name="android.permission.REORDER_TASKS" />
```

2.加入服务以及广播

```
 <!--保活相关配置-->
        <receiver android:name="com.townwang.keepalive.receiver.NotificationClickReceiver" />
        <activity android:name="com.townwang.keepalive.activity.OnePixelActivity" />

        <service android:name="com.townwang.keepalive.service.LocalService" />
        <service android:name="com.townwang.keepalive.service.HideForegroundService" />
        <service
                android:name="com.townwang.keepalive.service.JobHandlerService"
                android:permission="android.permission.BIND_JOB_SERVICE" />
        <service
                android:name="com.townwang.keepalive.service.RemoteService"
                android:process=":remote" />
```

3. 在Appliction 初始化

```
 KeepLive.startWork(this, KeepLive.RunMode.ROGUE, ForegroundNotification("进程保活", "正在运行...",
            R.mipmap.ic_launcher, object : ForegroundNotificationClickListener {
                override fun foregroundNotificationClick(context: Context, intent: Intent) {
                    //点击通知回调

                }
            }), object : KeepLiveService {
            override fun onStop() {
                //可能调用多次，跟onWorking匹配调用
            }

            override fun onWorking() {
                //一直存活，可能调用多次
            }
        })
```
### APP内更新

正在收集或开发...