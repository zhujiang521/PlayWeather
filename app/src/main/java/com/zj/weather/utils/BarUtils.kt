package com.zj.weather.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.Type.statusBars

/**
 * 设置透明状态栏
 */
fun Activity.transparentStatusBar() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
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

/**
 * 隐藏ime
 */
fun Activity?.hideIme() {
    if (this == null || window == null) return
    val controller = ViewCompat.getWindowInsetsController(window.decorView)
    controller?.hide(ime())
}

/**
 * 显示ime
 */
fun Activity?.showIme() {
    if (this == null || window == null) return
    val controller = ViewCompat.getWindowInsetsController(window.decorView)
    controller?.show(ime())
}
