package com.townwang.sample

import android.app.Activity
import android.os.Bundle
import com.townwang.update.UpdateHelper

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        UpdateHelper.VersionInfo(this)
            .setUrl("https://townwang.com/town.apk")
            .setNewVersion("1.0.1").setForce(true)
            .build()

    }


    override fun onDestroy() {
        super.onDestroy()

    }
}
