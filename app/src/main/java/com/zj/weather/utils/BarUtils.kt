package com.zj.weather.utils

import android.app.Activity
import android.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.statusBars

/**
 * 设置透明状态栏
 */
fun Activity.transparentStatusBar() {
    val controller = ViewCompat.getWindowInsetsController(window.decorView)
    controller?.hide(statusBars())
    window.statusBarColor = Color.TRANSPARENT
}

/**
 * 状态栏反色
 */
fun Activity.setAndroidNativeLightStatusBar() {
    val controller = ViewCompat.getWindowInsetsController(window.decorView)
    controller?.isAppearanceLightStatusBars = !isDarkMode()
}