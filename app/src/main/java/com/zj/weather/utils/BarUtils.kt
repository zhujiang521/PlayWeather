package com.zj.weather.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.core.view.ViewCompat

/**
 * 设置透明状态栏
 */
fun Activity.transparentStatusBar() {
    val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    val vis = window.decorView.systemUiVisibility
    window.decorView.systemUiVisibility = option or vis
    window.statusBarColor = Color.TRANSPARENT
}

/**
 * 状态栏反色
 */
fun Activity.setAndroidNativeLightStatusBar() {
    val controller = ViewCompat.getWindowInsetsController(window.decorView)
    controller?.isAppearanceLightStatusBars = !isDarkMode()
}