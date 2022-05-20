package com.zj.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zj.utils.setAndroidNativeLightStatusBar
import com.zj.utils.transparentStatusBar

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    /**
     * 初始化View
     */
    private fun initView() {
        // 加载动画
        installSplashScreen()
        // 状态栏透明
        transparentStatusBar()
        // 状态栏反色
        setAndroidNativeLightStatusBar()
    }

}