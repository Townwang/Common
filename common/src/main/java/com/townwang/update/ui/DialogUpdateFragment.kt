@file:Suppress("DEPRECATION")

package com.townwang.update.ui

import android.annotation.SuppressLint
import android.app.DialogFragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.townwang.R
import com.townwang.update.UpdateHelper
import com.townwang.update.config.CommitUtils
import kotlinx.android.synthetic.main.fragment_dialog_update.*


/**
 * 是否更新对话框
 */

@SuppressLint("NewApi")
class DialogUpdateFragment : DialogFragment() {
    private val broadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(context: Context?, intent: Intent?) {
                intent ?: return
                when (intent.action) {
                    "dialog_te" -> {
                        progress_bar.progress = intent.getIntExtra("dialog_te", 0)
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        isCancelable = false
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        val style = STYLE_NO_TITLE
        val theme = 0
        setStyle(style, theme)

        context.registerReceiver(broadcastReceiver, IntentFilter().apply {
            addAction("dialog_te")
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val attributes = dialog.window!!.attributes
        attributes.width = CommitUtils.getWidth(activity)
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = attributes
        dialog.window!!.setBackgroundDrawableResource(R.drawable.shape_rectangle_black_2d2d2d_bg)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVersionInfo = arguments.getSerializable("mVersionInfo") as UpdateHelper.VersionInfo
        vTitle.text = mVersionInfo.titile
        vContent.text = mVersionInfo.content
        vNo.text = mVersionInfo.btnNo
        vOk.text = mVersionInfo.btnOk
        if (mVersionInfo.isForce) {
            vOk.setOnClickListener {
                progress_bar.visibility = View.VISIBLE
                vContent.visibility = View.GONE
                vOk.visibility = View.GONE
                UpdateHelper.startUpdate(mVersionInfo)
            }
            vNo.visibility = View.GONE
        } else {
            vOk.setOnClickListener {
                dismiss()
                UpdateHelper.startUpdate(mVersionInfo)
            }
            vNo.setOnClickListener {
                dismiss()
            }
            vNo.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_update, container, false)
    }

    companion object {
        private lateinit var mVersionInfo: UpdateHelper.VersionInfo
        @SuppressLint("NewApi")
        fun newInstance(mVersionInfo: UpdateHelper.VersionInfo): DialogUpdateFragment {
            val instance = DialogUpdateFragment()
            val args = Bundle()
            args.putSerializable("mVersionInfo", mVersionInfo)
            instance.arguments = args
            return instance
        }
    }
}
