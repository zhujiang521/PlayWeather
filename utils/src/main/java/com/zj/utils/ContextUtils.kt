package com.zj.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.zj.model.Lang

/**
 * 网络状态
 * 判断手机接入点（APN）是否处于可以使用的状态
 *
 * @return 是否可用，数据网络或者wifi任意一个有就返回true，反之返回false
 */
fun Context.checkNetConnect(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val networkCapabilities = connectivityManager?.getNetworkCapabilities(
        connectivityManager.activeNetwork
    )
    return when {
        networkCapabilities == null -> {
            false
        }

        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
            // 当前使用移动网络
            true
        }

        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
            // 当前使用WIFI网络
            true
        }

        else -> {
            false
        }
    }
}


/**
 * 获取默认语言
 */
fun Context.getDefaultLocale(): Lang {
    XLog.d("getDefaultLocale: ${resources.configuration.locales[0].toLanguageTag()}")
    return when (resources.configuration.locales[0].toLanguageTag()) {
        "zh", "zh-CN", "zh-Hans-CN" -> Lang.ZH_HANS
        "zh_rHK", "zh_rTW", "zh_HK", "zh_TW", "HK", "TW", "zh-TW", "zh-HK" -> Lang.ZH_HANT
        "es", "en" -> Lang.EN
        else -> Lang.EN
    }
}


/**
 * 获取当前是否为深色模式
 * 深色模式的值为:0x21
 * 浅色模式的值为:0x11
 * @return true 为是深色模式   false为不是深色模式
 */
fun Context.isDarkMode(): Boolean {
    return resources.configuration.uiMode == 0x21
}