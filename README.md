# Common
[![Download](https://api.bintray.com/packages/townwang/Common/common/images/download.svg) ](https://bintray.com/townwang/Common/common/_latestVersion)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

>>安卓中通用的工具库，自用

## 一句话引入

```
api 'com.townwang:common:x.x.x' //x.x.x 换成最新版本
```
## 功能

### 进程保活

#### 使用方法

 在Appliction 初始化

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

#### 使用方法

 1. 在更新Activity初始化
 
```
UpdateHelper.VersionInfo(this)
            .setUrl("https://townwang.com/town.apk") //app下载地址
            .setNewVersion("1.0.1")// 版本号  或者 setNewVersionCode 版本Code
            .setForce(true) //s是否强制更新 默认非强制
            .build()
```